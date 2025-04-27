package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.firebase_auth_invalid_credentials_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_invalid_user_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_user_collision_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_cancellation_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_interrupted_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_unknown_exception_message
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
    val isAuthenticated: Boolean = false
)

sealed interface AuthenticationEvents {
    data class OnEmailTextFieldValueChanged(val value: String) : AuthenticationEvents
    data class OnPasswordTextFieldValueChanged(val value: String) : AuthenticationEvents
    data object OnSignInWithXButtonClicked : AuthenticationEvents
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

            AuthenticationEvents.OnSignInWithXButtonClicked -> {
                viewModelScope.launch {
                    firebaseAuthentication.authenticateWithX()
                }
            }

        }
    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
    }

}