package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.firebase_auth_invalid_credentials_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_invalid_user_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_too_many_requestes_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_user_collision_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_cancellation_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_interrupted_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_unknown_exception_message
import knowyourfan.composeapp.generated.resources.invalid_email_error_message
import knowyourfan.composeapp.generated.resources.password_empty_field_error_message
import knowyourfan.composeapp.generated.resources.sign_in_invalid_credentials_message
import knowyourfan.composeapp.generated.resources.unknown_error_occurred_message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

data class AuthenticationState(
    val emailTextField: String? = null,
    val passwordTextField: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: StringResource? = null,
    val isAuthenticated: Boolean = false,
)

sealed interface AuthenticationEvents {
    data class OnEmailTextFieldValueChanged(val value: String) : AuthenticationEvents
    data class OnPasswordTextFieldValueChanged(val value: String) : AuthenticationEvents
    data object OnSignInButtonClicked : AuthenticationEvents
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
                viewModelScope.launch {

                    val email = _state.value.emailTextField
                    val password = _state.value.passwordTextField

                    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()

                    if (!email.isNullOrEmpty() && email.matches(emailRegex) && !password.isNullOrEmpty()) {
                        _state.update { it.copy(isLoading = true) }


                        when (val result = firebaseAuthentication.signInUser(email, password)) {
                            is Result.Success -> {
                                _state.update { it.copy(isAuthenticated = true) }
                            }
                            is Result.Error -> {
                                val errorMessage = when(result.error) {
                                    AuthenticationError.SignIn.USER_NOT_FOUND -> Res.string.firebase_auth_user_collision_exception_message
                                    AuthenticationError.SignIn.INVALID_CREDENTIALS -> Res.string.sign_in_invalid_credentials_message
                                    AuthenticationError.SignIn.TOO_MANY_REQUESTS -> Res.string.firebase_auth_too_many_requestes_exception_message
                                    AuthenticationError.SignIn.UNKNOWN -> Res.string.unknown_error_occurred_message
                                }

                                _state.update { it.copy(errorMessage = errorMessage) }

                            }
                            Result.Loading -> {}
                        }


                    } else {
                        if (password.isNullOrEmpty()) {
                            _state.update { it.copy(errorMessage = Res.string.password_empty_field_error_message) }
                        }
                        if (!email.isNullOrEmpty() && !email.matches(emailRegex)) {
                            _state.update { it.copy(errorMessage = Res.string.invalid_email_error_message) }
                        }
                    }

                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
    }

}