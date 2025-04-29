package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.GetCredentialUnknownException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.TwitterAuthCredential
import com.google.firebase.auth.TwitterAuthProvider
import com.guilherme.knowyourfan.MainActivity
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.presentation.theme.provider
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class FirebaseAuthenticationImpl(
    private val auth: FirebaseAuth,
) : FirebaseAuthentication {

    override suspend fun signUpUser(
        email: String,
        password: String,
    ): Result<Unit, AuthenticationError.Authentication> {

        return try {

            auth.createUserWithEmailAndPassword(email, password).await()

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