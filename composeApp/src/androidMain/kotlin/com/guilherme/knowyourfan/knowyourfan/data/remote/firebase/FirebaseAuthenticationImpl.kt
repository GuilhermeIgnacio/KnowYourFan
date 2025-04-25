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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class FirebaseAuthenticationImpl(
    private val context: Context,
    private val auth: FirebaseAuth
): FirebaseAuthentication {

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }

    private suspend fun getGoogleIdTokenCredential(): Result<AuthCredential, AuthenticationError.Authentication> {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("716394498537-2a062viguauoqrh33tn41fvpfn5luho2.apps.googleusercontent.com")
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )

            val credential = result.credential

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)

            val foo = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

            Result.Success(foo)

        } catch (e: Exception) {
            e.printStackTrace()
            val error = when (e) {
                is GetCredentialUnknownException -> AuthenticationError.Authentication.GET_CREDENTIAL_UNKNOWN
                is GetCredentialCancellationException -> AuthenticationError.Authentication.GET_CREDENTIAL_CANCELLATION
                is GetCredentialInterruptedException -> AuthenticationError.Authentication.GET_CREDENTIAL_INTERRUPTED
                is GetCredentialException -> AuthenticationError.Authentication.GET_CREDENTIAL_EXCEPTION
                else -> AuthenticationError.Authentication.UNKNOWN
            }

            Result.Error(error)

        }
    }


    override suspend fun authenticateWithGoogle(): Result<Unit, AuthenticationError.Authentication> {

        return when (val result = getGoogleIdTokenCredential()) {
            is Result.Success -> {
                try {

                    auth.signInWithCredential(result.data).await()
                    Result.Success(Unit)

                } catch (e: Exception) {
                    e.printStackTrace()
                    val error = when (e) {
                        is FirebaseAuthInvalidUserException -> AuthenticationError.Authentication.FIREBASE_AUTH_INVALID_USER
                        is FirebaseAuthInvalidCredentialsException -> AuthenticationError.Authentication.FIREBASE_AUTH_INVALID_CREDENTIALS
                        is FirebaseAuthUserCollisionException -> AuthenticationError.Authentication.FIREBASE_AUTH_USER_COLLISION
                        else -> AuthenticationError.Authentication.UNKNOWN
                    }
                    Result.Error(error)
                }
            }

            Result.Loading -> Result.Loading

            is Result.Error -> result
        }
    }
}