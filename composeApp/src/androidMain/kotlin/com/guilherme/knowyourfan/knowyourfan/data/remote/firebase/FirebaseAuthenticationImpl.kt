package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase

import android.content.ActivityNotFoundException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialUnknownException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthWebException
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.TwitterAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import com.guilherme.knowyourfan.MainActivity
import com.guilherme.knowyourfan.core.domain.DatabaseError
import com.guilherme.knowyourfan.core.domain.LinkingError
import com.guilherme.knowyourfan.core.domain.UserCheckError
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.model.UserInterests
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticationImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    val activity: MainActivity,
) : FirebaseAuthentication {

    override suspend fun signUpUser(
        email: String,
        password: String,
        purchases: List<String>,
        events: List<String>,
        interestGames: List<String>,
    ): Result<Unit, AuthenticationError.SignUp> {

        return try {

            val foo = auth.createUserWithEmailAndPassword(email, password).await()

            val user = hashMapOf(
                "purchases" to purchases,
                "events" to events,
                "interestGames" to interestGames
            )

            foo.user?.uid?.let { uid ->
                db.collection("users").document(uid).set(user).await()
            }

            Result.Success(Unit)

        } catch (e: Exception) {
            val error = when (e) {
                is GetCredentialUnknownException -> AuthenticationError.SignUp.GET_CREDENTIAL_UNKNOWN
                is GetCredentialCancellationException -> AuthenticationError.SignUp.GET_CREDENTIAL_CANCELLATION
                is GetCredentialInterruptedException -> AuthenticationError.SignUp.GET_CREDENTIAL_INTERRUPTED
                is FirebaseAuthInvalidUserException -> AuthenticationError.SignUp.FIREBASE_AUTH_INVALID_USER
                is FirebaseAuthWeakPasswordException -> AuthenticationError.SignUp.FIREBASE_AUTH_WEAK_PASSWORD
                is FirebaseAuthInvalidCredentialsException -> AuthenticationError.SignUp.FIREBASE_AUTH_INVALID_CREDENTIALS
                is FirebaseAuthUserCollisionException -> AuthenticationError.SignUp.FIREBASE_AUTH_USER_COLLISION
                is GetCredentialException -> AuthenticationError.SignUp.GET_CREDENTIAL_EXCEPTION
                else -> AuthenticationError.SignUp.UNKNOWN
            }
            e.printStackTrace()
            Result.Error(error)
        }

    }

    override suspend fun signInUser(
        email: String,
        password: String,
    ): Result<Unit, AuthenticationError.SignIn> {

        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            val error = when (e) {
                is FirebaseAuthInvalidUserException -> AuthenticationError.SignIn.USER_NOT_FOUND
                is FirebaseAuthInvalidCredentialsException -> AuthenticationError.SignIn.INVALID_CREDENTIALS
                is FirebaseTooManyRequestsException -> AuthenticationError.SignIn.TOO_MANY_REQUESTS
                else -> AuthenticationError.SignIn.UNKNOWN
            }

            e.printStackTrace()
            println(error)

            Result.Error(error)

        }

    }

    override suspend fun isAccountLinkedToX(): Result<Boolean, UserCheckError.User> {

        val currentUser = auth.currentUser

        return if (currentUser != null) {
            val foo =
                currentUser.providerData.any { it.providerId == TwitterAuthProvider.PROVIDER_ID }
            Result.Success(foo)
        } else {
            Result.Error(UserCheckError.User.NULL_USER)
        }

    }

    override suspend fun linkAccountToX(): Result<Unit, LinkingError.Twitter> {
        val user = auth.currentUser
            ?: return Result.Error(LinkingError.Twitter.NULL_USER)

        return runCatching {
            val linkResult = user
                .startActivityForLinkWithProvider(
                    activity,
                    OAuthProvider.newBuilder("twitter.com").build()
                )
                .await()

            val credential = linkResult.credential
                ?: throw IllegalStateException()

            user.linkWithCredential(credential).await()
        }.fold(
            onSuccess = {
                Result.Success(Unit)
            },
            onFailure = { e ->
                e.printStackTrace()
                when (e) {
                    is IllegalArgumentException -> Result.Error(LinkingError.Twitter.NULL_CREDENTIALS)
                    is FirebaseAuthMultiFactorException -> Result.Error(LinkingError.Twitter.MFA_REQUIRED)
                    is ActivityNotFoundException -> Result.Error(LinkingError.Twitter.NO_BROWSER_FOUND)
                    is FirebaseAuthUserCollisionException -> Result.Error(LinkingError.Twitter.ACCOUNT_ALREADY_LINKED)
                    is FirebaseAuthWebException -> Result.Error(LinkingError.Twitter.WEB_ERROR)
                    is FirebaseNetworkException -> Result.Error(LinkingError.Twitter.NETWORK_ERROR)
                    is FirebaseTooManyRequestsException -> Result.Error(LinkingError.Twitter.RATE_LIMITED)
                    else -> Result.Error(LinkingError.Twitter.UNKNOWN)
                }
            }
        )
    }

    override suspend fun getUserInterests(): Result<UserInterests, DatabaseError.DatabaseRead> {

        val currentUser = auth.currentUser

        return try {
            if (currentUser != null) {
                val snapshot = db.collection("users").document(currentUser.uid).get().await()
                val userInterests = snapshot.toObject<UserInterests>() ?: UserInterests()

                Result.Success(userInterests)
            } else {
                Result.Error(DatabaseError.DatabaseRead.NULL_USER)
            }
        } catch (e: FirebaseFirestoreException) {
            e.printStackTrace()
            val error = when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> DatabaseError.DatabaseRead.PERMISSION_DENIED
                FirebaseFirestoreException.Code.NOT_FOUND -> DatabaseError.DatabaseRead.NOT_FOUND
                else -> DatabaseError.DatabaseRead.UNKNOWN
            }
            Result.Error(error)
        }


    }

}