package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.knowyourfan.core.domain.GeminiError
import com.guilherme.knowyourfan.core.util.toBase64
import com.guilherme.knowyourfan.domain.AuthenticationError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.GeminiService
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseStorage
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.client_request_exception
import knowyourfan.composeapp.generated.resources.cpf_does_not_matches
import knowyourfan.composeapp.generated.resources.firebase_auth_invalid_credentials_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_invalid_user_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_user_collision_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_weak_password_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_cancellation_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_interrupted_exception_message
import knowyourfan.composeapp.generated.resources.get_credential_unknown_exception_message
import knowyourfan.composeapp.generated.resources.redirect_response_exception
import knowyourfan.composeapp.generated.resources.serialization_exception
import knowyourfan.composeapp.generated.resources.server_response_exception
import knowyourfan.composeapp.generated.resources.unknown_error_occurred_message
import knowyourfan.composeapp.generated.resources.unresolved_address_exception
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

data class SignUpState(
    val usernameTextField: String? = null,
    val emailTextField: String? = null,

    val confirmEmailTextField: String? = null,
    val idTextField: String? = null,

    val passwordTextField: String? = null,
    val confirmPasswordTextField: String? = null,

    val lastYearPurchasesTextField: String? = null,
    val eventsTextField: String? = null,

    val interestGamesList: List<ChipItem> = emptyList(),
    val lastYearPurchasesList: List<ChipItem> = emptyList(),
    val eventsList: List<ChipItem> = emptyList(),

    val selectedImageByteArray: ByteArray? = null,

    val isLoading: Boolean = false,
    val errorMessage: StringResource? = null,
    val isAuthenticated: Boolean = false,
)

sealed interface SignUpEvents {
    data class OnUsernameTextFieldValueChanged(val value: String) : SignUpEvents

    data class OnEmailTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnConfirmEmailTextFieldValueChanged(val value: String) : SignUpEvents

    data class OnPasswordTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnConfirmPasswordTextFieldValueChanged(val value: String) : SignUpEvents

    data class OnIdTextFieldValueChanged(val value: String) : SignUpEvents

    data class OnLastYearPurchasesTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnLastYearPurchaseChipClicked(val value: ChipItem) : SignUpEvents

    data class OnEventsTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnEventChipClicked(val value: ChipItem) : SignUpEvents

    data class OnGameChipClicked(val value: ChipItem) : SignUpEvents

    data class OnImageSelected(val value: ByteArray) : SignUpEvents
    data object OnRemoveImageSelected : SignUpEvents

    data object OnRegisterButtonClicked : SignUpEvents
}

class SignUpViewModel(
    private val firebaseAuthentication: FirebaseAuthentication,
    private val firebaseStorage: FirebaseStorage,
    private val geminiService: GeminiService,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignUpEvents) {
        when (event) {
            is SignUpEvents.OnUsernameTextFieldValueChanged -> {

                val usernameRegex = Regex("^[a-zA-Z0-9]*$")

                if (event.value.matches(usernameRegex) && event.value.length <= 16) {
                    _state.update {
                        it.copy(
                            usernameTextField = event.value
                        )
                    }
                }
            }

            is SignUpEvents.OnEmailTextFieldValueChanged -> {
                _state.update {
                    it.copy(
                        emailTextField = event.value
                    )
                }
            }

            is SignUpEvents.OnConfirmEmailTextFieldValueChanged -> {
                _state.update {
                    it.copy(
                        confirmEmailTextField = event.value
                    )
                }
            }

            is SignUpEvents.OnPasswordTextFieldValueChanged -> {
                _state.update {
                    it.copy(
                        passwordTextField = event.value
                    )
                }
            }

            is SignUpEvents.OnConfirmPasswordTextFieldValueChanged -> {
                _state.update {
                    it.copy(
                        confirmPasswordTextField = event.value
                    )
                }
            }

            is SignUpEvents.OnIdTextFieldValueChanged -> {

                if (event.value.length <= 11) {
                    _state.update {
                        it.copy(
                            idTextField = event.value.filter { char -> char.isDigit() }
                        )
                    }
                }

            }

            is SignUpEvents.OnGameChipClicked -> {
                val mutableList = _state.value.interestGamesList.toMutableList()

                if (event.value !in mutableList) {
                    mutableList.add(event.value)
                } else {
                    mutableList.remove(event.value)
                }

                _state.update { it.copy(interestGamesList = mutableList) }
            }

            SignUpEvents.OnRemoveImageSelected -> {
                _state.update {
                    it.copy(
                        selectedImageByteArray = null
                    )
                }
            }

            is SignUpEvents.OnImageSelected -> {
                _state.update {
                    it.copy(
                        selectedImageByteArray = event.value
                    )
                }
            }

            SignUpEvents.OnRegisterButtonClicked -> {
                viewModelScope.launch {
                    val imageByteArray = _state.value.selectedImageByteArray
                    val idTextField = _state.value.idTextField

                    if (imageByteArray != null && !idTextField.isNullOrEmpty()) {
                        _state.update { it.copy(isLoading = true) }
                        when (val result = geminiService.analyzeImage(
                            image = imageByteArray.toBase64(),
                            id = idTextField
                        )) {
                            is Result.Success -> {

                                val response = result.data
                                if (!response.isNullOrEmpty()) {
                                    if (response.contains("true", ignoreCase = true)) {

                                        val email = _state.value.emailTextField
                                        val password = _state.value.passwordTextField

                                        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                                            when (val authenticationResult =
                                                firebaseAuthentication.signUpUser(
                                                    email = email,
                                                    password = password
                                                )) {
                                                is Result.Success -> {
                                                    _state.update { it.copy(isAuthenticated = true) }
                                                }

                                                is Result.Error -> {
                                                    val errorMessage =
                                                        when (authenticationResult.error) {
                                                            AuthenticationError.Authentication.GET_CREDENTIAL_EXCEPTION -> Res.string.get_credential_unknown_exception_message
                                                            AuthenticationError.Authentication.GET_CREDENTIAL_UNKNOWN -> Res.string.get_credential_unknown_exception_message
                                                            AuthenticationError.Authentication.GET_CREDENTIAL_CANCELLATION -> Res.string.get_credential_cancellation_exception_message
                                                            AuthenticationError.Authentication.GET_CREDENTIAL_INTERRUPTED -> Res.string.get_credential_interrupted_exception_message
                                                            AuthenticationError.Authentication.FIREBASE_AUTH_INVALID_USER -> Res.string.firebase_auth_invalid_user_exception_message
                                                            AuthenticationError.Authentication.FIREBASE_AUTH_INVALID_CREDENTIALS -> Res.string.firebase_auth_invalid_credentials_exception_message
                                                            AuthenticationError.Authentication.FIREBASE_AUTH_USER_COLLISION -> Res.string.firebase_auth_user_collision_exception_message
                                                            AuthenticationError.Authentication.FIREBASE_AUTH_WEAK_PASSWORD -> Res.string.firebase_auth_weak_password_exception_message
                                                            AuthenticationError.Authentication.UNKNOWN -> Res.string.unknown_error_occurred_message
                                                        }

                                                    _state.update {
                                                        it.copy(
                                                            isLoading = false,
                                                            errorMessage = errorMessage
                                                        )
                                                    }

                                                }

                                                Result.Loading -> TODO()
                                            }
                                        }

                                    } else if (response.contains("false", ignoreCase = true)) {
                                        _state.update { it.copy(errorMessage = Res.string.cpf_does_not_matches) }
                                    }
                                }

                                _state.update { it.copy(isLoading = false) }

                            }

                            is Result.Error -> {
                                val errorMessage = when (result.error) {
                                    GeminiError.Gemini.SERIALIZATION -> Res.string.serialization_exception
                                    GeminiError.Gemini.REDIRECT_RESPONSE -> Res.string.redirect_response_exception
                                    GeminiError.Gemini.CLIENT_REQUEST -> Res.string.client_request_exception
                                    GeminiError.Gemini.SERVER_RESPONSE -> Res.string.server_response_exception
                                    GeminiError.Gemini.UNRESOLVED_ADDRESS -> Res.string.unresolved_address_exception
                                    GeminiError.Gemini.IO -> Res.string.unknown_error_occurred_message
                                    GeminiError.Gemini.UNKNOWN -> Res.string.unknown_error_occurred_message
                                }

                                _state.update {
                                    it.copy(
                                        errorMessage = errorMessage,
                                        isLoading = false
                                    )
                                }

                            }

                            Result.Loading -> {}
                        }
                    }

                }

            }

            is SignUpEvents.OnLastYearPurchasesTextFieldValueChanged -> {
                if (event.value.contains(",")) {

                    val chipItem = ChipItem(
                        game = event.value.replace(",", ""),
                        onClick = {},
                        icon = null
                    )

                    val list = _state.value.lastYearPurchasesList.toMutableList()
                    list.add(chipItem)

                    _state.update { it.copy(lastYearPurchasesList = list, lastYearPurchasesTextField = null) }

                } else {
                    _state.update { it.copy(lastYearPurchasesTextField = event.value) }
                }
            }

            is SignUpEvents.OnLastYearPurchaseChipClicked -> {
                val list = _state.value.lastYearPurchasesList.toMutableList()
                list.remove(event.value)
                _state.update { it.copy(lastYearPurchasesList = list) }
            }

            is SignUpEvents.OnEventsTextFieldValueChanged -> {
                if (event.value.contains(",")) {

                    val chipItem = ChipItem(
                        game = event.value.replace(",", ""),
                        onClick = {},
                        icon = null
                    )

                    val list = _state.value.eventsList.toMutableList()
                    list.add(chipItem)

                    _state.update { it.copy(eventsList = list, eventsTextField = null) }

                } else {
                    _state.update { it.copy(eventsTextField = event.value) }
                }
            }

            is SignUpEvents.OnEventChipClicked -> {
                val list = _state.value.eventsList.toMutableList()
                list.remove(event.value)
                _state.update { it.copy(eventsList = list) }
            }
        }
    }

    fun clearSnackBar() {
        _state.update { it.copy(errorMessage = null) }
    }

}