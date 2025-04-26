package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.lifecycle.ViewModel
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SignUpState(
    val usernameTextField: String? = null,
    val emailTextField: String? = null,
    val confirmEmailTextField: String? = null,
    val idTextField: String? = null,
    val passwordTextField: String? = null,
    val confirmPasswordTextField: String? = null,
    val interestGamesList: List<ChipItem> = emptyList(),
)

sealed interface SignUpEvents {
    data class OnUsernameTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnEmailTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnConfirmEmailTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnIdTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnPasswordTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnConfirmPasswordTextFieldValueChanged(val value: String) : SignUpEvents
    data class OnGameChipClicked(val value: ChipItem) : SignUpEvents
}

class SignUpViewModel(
    private val firebaseAuthentication: FirebaseAuthentication,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignUpEvents) {
        when (event) {
            is SignUpEvents.OnUsernameTextFieldValueChanged -> {
                _state.update {
                    it.copy(
                        usernameTextField = event.value
                    )
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
                _state.update {
                    it.copy(
                        idTextField = event.value
                    )
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
        }
    }

}