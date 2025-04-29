package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase

import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialUnknownException
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
    ): Result<Unit, AuthenticationError.Authentication> {

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
                is GetCredentialUnknownException -> AuthenticationError.Authentication.GET_CREDENTIAL_UNKNOWN
                is GetCredentialCancellationException -> AuthenticationError.Authentication.GET_CREDENTIAL_CANCELLATION
                is GetCredentialInterruptedException -> AuthenticationError.Authentication.GET_CREDENTIAL_INTERRUPTED
                is FirebaseAuthInvalidUserException -> AuthenticationError.Authentication.FIREBASE_AUTH_INVALID_USER
                is FirebaseAuthWeakPasswordException -> AuthenticationError.Authentication.FIREBASE_AUTH_WEAK_PASSWORD
                is FirebaseAuthInvalidCredentialsException -> AuthenticationError.Authentication.FIREBASE_AUTH_INVALID_CREDENTIALS
                is FirebaseAuthUserCollisionException -> AuthenticationError.Authentication.FIREBASE_AUTH_USER_COLLISION
                is GetCredentialException -> AuthenticationError.Authentication.GET_CREDENTIAL_EXCEPTION
                else -> AuthenticationError.Authentication.UNKNOWN
            }
            e.printStackTrace()
            Result.Error(error)
        }

    }
}