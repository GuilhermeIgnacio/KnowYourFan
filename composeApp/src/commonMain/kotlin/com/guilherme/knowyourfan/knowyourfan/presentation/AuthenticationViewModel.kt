package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthenticationState(
    val emailTextField: String? = null,
    val passwordTextField: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface AuthenticationEvents {
    data class OnEmailTextFieldValueChanged(val value: String) : AuthenticationEvents
    data class OnPasswordTextFieldValueChanged(val value: String) : AuthenticationEvents
    data object OnSignInButtonClicked : AuthenticationEvents
    data object OnSignInWithGoogleButtonClicked : AuthenticationEvents
    data object OnSignInWithAppleButtonClicked : AuthenticationEvents
}

class AuthenticationViewModel(
    private val firebaseAuthentication: FirebaseAuthentication,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthenticationState())
    val state = _state.asStateFlow()

    fun onEvent(event: AuthenticationEvents) {
        when (event) {
            is AuthenticationEvents.OnEmailTextFieldValueChanged -> {
                _state.update {
                    it.copy(emailTextField = event.value)
                }
            }

            is AuthenticationEvents.OnPasswordTextFieldValueChanged -> {
                _state.update {
                    it.copy(
                        passwordTextField = event.value
                    )
                }
            }

            AuthenticationEvents.OnSignInButtonClicked -> {

            }

            AuthenticationEvents.OnSignInWithGoogleButtonClicked -> {

                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    when (val result = firebaseAuthentication.authenticateWithGoogle()) {

                        is Result.Success -> {
                            println("Success")
                        }

                        is Result.Error -> {
                            val errorMessage = when (result.error) {
                                AuthenticationError.Authentication.GET_CREDENTIAL_EXCEPTION -> "TODO()"
                                AuthenticationError.Authentication.GET_CREDENTIAL_UNKNOWN -> "TODO()"
                                AuthenticationError.Authentication.GET_CREDENTIAL_CANCELLATION -> "TODO()"
                                AuthenticationError.Authentication.GET_CREDENTIAL_INTERRUPTED -> "TODO()"
                                AuthenticationError.Authentication.FIREBASE_AUTH_INVALID_USER -> "TODO()"
                                AuthenticationError.Authentication.FIREBASE_AUTH_INVALID_CREDENTIALS -> "TODO()"
                                AuthenticationError.Authentication.FIREBASE_AUTH_USER_COLLISION -> "TODO()"
                                AuthenticationError.Authentication.UNKNOWN -> "TODO()"
                            }

                            _state.update { it.copy(errorMessage = errorMessage, isLoading = false) }

                        }

                        Result.Loading -> {}
                    }
                }
            }

            AuthenticationEvents.OnSignInWithAppleButtonClicked -> {

            }

        }
    }

}