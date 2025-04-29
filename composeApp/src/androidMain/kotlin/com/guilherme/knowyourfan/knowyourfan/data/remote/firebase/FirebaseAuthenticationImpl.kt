package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase

import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialUnknownException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticationImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
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
}