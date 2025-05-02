package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.knowyourfan.core.domain.LinkingError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import com.guilherme.knowyourfan.knowyourfan.domain.RecommendationRepository
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.account_link_success
import knowyourfan.composeapp.generated.resources.already_linked_account_exception_message
import knowyourfan.composeapp.generated.resources.firebase_auth_too_many_requestes_exception_message
import knowyourfan.composeapp.generated.resources.getting_linked_accounts_error_message
import knowyourfan.composeapp.generated.resources.mfa_exception_message
import knowyourfan.composeapp.generated.resources.network_exception_message
import knowyourfan.composeapp.generated.resources.no_browser_found_exception_message
import knowyourfan.composeapp.generated.resources.null_credentials_exception_message
import knowyourfan.composeapp.generated.resources.null_user_exception_message
import knowyourfan.composeapp.generated.resources.unknown_error_occurred_message
import knowyourfan.composeapp.generated.resources.web_error_exception_message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

data class ProfileState(
    val isLinkedWithX: Boolean = false,
    val errorMessage: StringResource? = null,
    val isLoading: Boolean = false,
)

sealed interface ProfileEvents {
    data object OnSignOutButtonClicked : ProfileEvents
    data object LinkXAccount : ProfileEvents
}

class ProfileViewModel(
    private val firebase: FirebaseAuthentication,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = firebase.isAccountLinkedToX()) {
                is Result.Success -> {
                    _state.update { it.copy(isLinkedWithX = result.data) }
                }

                is Result.Error -> {

                    val errorMessage = Res.string.getting_linked_accounts_error_message

                    _state.update { it.copy(errorMessage = errorMessage) }
                }

                Result.Loading -> {}
            }
        }
    }

    fun onEvent(event: ProfileEvents) {
        when (event) {
            ProfileEvents.LinkXAccount -> {
                viewModelScope.launch {

                    if (!_state.value.isLinkedWithX) {
                        when (val result = firebase.linkAccountToX()) {
                            is Result.Success -> {
                                _state.update {
                                    it.copy(
                                        isLinkedWithX = true,
                                        errorMessage = Res.string.account_link_success
                                    )
                                }
                            }

                            is Result.Error -> {
                                val errorMessage = when (result.error) {
                                    LinkingError.Twitter.NULL_USER -> Res.string.null_user_exception_message
                                    LinkingError.Twitter.NULL_CREDENTIALS -> Res.string.null_credentials_exception_message
                                    LinkingError.Twitter.MFA_REQUIRED -> Res.string.mfa_exception_message
                                    LinkingError.Twitter.NO_BROWSER_FOUND -> Res.string.no_browser_found_exception_message
                                    LinkingError.Twitter.ACCOUNT_ALREADY_LINKED -> Res.string.already_linked_account_exception_message
                                    LinkingError.Twitter.WEB_ERROR -> Res.string.web_error_exception_message
                                    LinkingError.Twitter.NETWORK_ERROR -> Res.string.network_exception_message
                                    LinkingError.Twitter.RATE_LIMITED -> Res.string.firebase_auth_too_many_requestes_exception_message
                                    LinkingError.Twitter.UNKNOWN -> Res.string.unknown_error_occurred_message
                                }

                                _state.update { it.copy(errorMessage = errorMessage) }

                            }

                            Result.Loading -> {}
                        }
                    }
                }
            }

            ProfileEvents.OnSignOutButtonClicked -> {
                viewModelScope.launch {
                    firebase.signOut()
                }
            }

        }
    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
    }

}