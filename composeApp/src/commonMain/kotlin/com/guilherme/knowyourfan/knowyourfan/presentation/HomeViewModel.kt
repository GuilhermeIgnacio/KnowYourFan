package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val isLinkedWithX: Boolean = false,
)

sealed interface HomeEvents {
    data object OnLinkWithXButtonClicked : HomeEvents
}

class HomeViewModel(
    private val firebaseAuthentication: FirebaseAuthentication,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    /*init {
        viewModelScope.launch {
            when (val result = firebaseAuthentication.isAccountLinkedToX()) {
                is Result.Success -> {
                    _state.update { it.copy(isLinkedWithX = result.data) }
                }

                is Result.Error -> {}
                Result.Loading -> {}
            }
        }
    }*/

    fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnLinkWithXButtonClicked -> {
                viewModelScope.launch {
                    firebaseAuthentication.linkAccountToX()
                }
            }
        }
    }


}