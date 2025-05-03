package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.knowyourfan.core.domain.DatabaseError
import com.guilherme.knowyourfan.core.domain.GeminiError
import com.guilherme.knowyourfan.core.domain.LinkingError
import com.guilherme.knowyourfan.core.domain.ParsingError
import com.guilherme.knowyourfan.core.domain.UserCheckError
import com.guilherme.knowyourfan.domain.Result
import com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.GeminiService
import com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.FirebaseAuthentication
import com.guilherme.knowyourfan.knowyourfan.domain.BrowserOpener
import com.guilherme.knowyourfan.knowyourfan.domain.DataStoreRepository
import com.guilherme.knowyourfan.knowyourfan.domain.Recommendation
import com.guilherme.knowyourfan.knowyourfan.domain.RecommendationRepository
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.already_linked_account_exception_message
import knowyourfan.composeapp.generated.resources.bad_request
import knowyourfan.composeapp.generated.resources.firebase_auth_too_many_requestes_exception_message
import knowyourfan.composeapp.generated.resources.firestore_not_found
import knowyourfan.composeapp.generated.resources.firestore_null_user
import knowyourfan.composeapp.generated.resources.firestore_permission_denied
import knowyourfan.composeapp.generated.resources.internal_server_error
import knowyourfan.composeapp.generated.resources.mfa_exception_message
import knowyourfan.composeapp.generated.resources.network_exception_message
import knowyourfan.composeapp.generated.resources.no_browser_found_exception_message
import knowyourfan.composeapp.generated.resources.null_credentials_exception_message
import knowyourfan.composeapp.generated.resources.null_user_exception_message
import knowyourfan.composeapp.generated.resources.parsing_error_message
import knowyourfan.composeapp.generated.resources.service_unavailable
import knowyourfan.composeapp.generated.resources.unauthorized
import knowyourfan.composeapp.generated.resources.unknown_error_occurred_message
import knowyourfan.composeapp.generated.resources.web_error_exception_message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import kotlin.time.ExperimentalTime

data class HomeState(
    val isLinkedWithX: Boolean = false,
    val errorMessage: StringResource? = null,
    val recommendations: List<Recommendation> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)

sealed interface HomeEvents {
    data object OnLinkWithXButtonClicked : HomeEvents
    data object OnContinueWithoutXButtonClicked : HomeEvents
    data object OnHomeScreenLoaded : HomeEvents
    data class OnCardClicked(val value: String) : HomeEvents
    data object OnTryAgainButtonClicked : HomeEvents
}

@OptIn(ExperimentalTime::class)
class HomeViewModel(
    private val firebaseAuthentication: FirebaseAuthentication,
    private val gemini: GeminiService,
    private val recommendation: RecommendationRepository,
    private val dataStore: DataStoreRepository,
    private val browserOpener: BrowserOpener,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = firebaseAuthentication.isAccountLinkedToX()) {
                is Result.Success -> {
                    _state.update { it.copy(isLinkedWithX = result.data) }
                }

                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        UserCheckError.User.NULL_USER -> Res.string.null_user_exception_message
                    }

                    _state.update { it.copy(errorMessage = errorMessage) }

                }

                Result.Loading -> {}
            }

        }
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnLinkWithXButtonClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    when (val result = firebaseAuthentication.linkAccountToX()) {
                        is Result.Success -> {
                            _state.update { it.copy(isLinkedWithX = true) }
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
                    _state.update { it.copy(isLoading = false) }
                }
            }

            is HomeEvents.OnCardClicked -> {
                browserOpener.openUrl(event.value)
            }

            HomeEvents.OnHomeScreenLoaded -> {
                viewModelScope.launch {
                    recommendation.fetchData()
                        .onStart {
                            if (dataStore.shouldCallApi()) {
                                getRecommendations()
                                println("API Called")
                            }
                        }
                        .catch { e -> /* log/error state */ }
                        .collect { recs ->
                            _state.update { it.copy(recommendations = recs) }
                        }
                }
            }

            HomeEvents.OnContinueWithoutXButtonClicked -> {
                _state.update { it.copy(isLinkedWithX = true) }
            }

            HomeEvents.OnTryAgainButtonClicked -> {
                getRecommendations()
            }
        }
    }

    fun clearErrorMessage() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun getRecommendations() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            when (val result = firebaseAuthentication.getUserInterests()) {
                is Result.Success -> {
                    when (val geminiResult = gemini.getRecommendations(result.data)) {
                        is Result.Success -> {

                            val rawRecommendationsList = geminiResult.data.candidates
                                .asSequence()
                                .mapNotNull { it.grounding }
                                .flatMap { grounding ->
                                    grounding.chunks.flatMap { chunk ->
                                        grounding.supports.map { support ->
                                            support.segment.text
                                        }
                                    }
                                }
                                .toList()

                            when (val parsingResult = parseRecommendation(rawRecommendationsList)) {
                                is Result.Success -> {
                                    recommendation.cacheData(parsingResult.data)
                                    dataStore.updateLastApiCallTime()
                                    _state.update { it.copy(isError = false) }
                                }

                                is Result.Error -> {
                                    val errorMessage = when (parsingResult.error) {
                                        ParsingError.ParsingRecommendations.UNKNOWN -> Res.string.parsing_error_message
                                    }

                                    _state.update {
                                        it.copy(
                                            errorMessage = errorMessage,
                                            isError = true
                                        )
                                    }

                                }

                                Result.Loading -> {}
                            }


                        }

                        is Result.Error -> {
                            val errorMessage = when (geminiResult.error) {
                                GeminiError.Recommendations.BAD_REQUEST -> Res.string.bad_request
                                GeminiError.Recommendations.UNAUTHORIZED -> Res.string.unauthorized
                                GeminiError.Recommendations.SERVER_ERROR -> Res.string.internal_server_error
                                GeminiError.Recommendations.SERVICE_UNAVAILABLE -> Res.string.service_unavailable
                                GeminiError.Recommendations.UNKNOWN -> Res.string.unknown_error_occurred_message
                            }

                            _state.update { it.copy(errorMessage = errorMessage, isError = true) }

                        }

                        Result.Loading -> {}
                    }
                }

                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        DatabaseError.DatabaseRead.NULL_USER -> Res.string.firestore_null_user
                        DatabaseError.DatabaseRead.PERMISSION_DENIED -> Res.string.firestore_permission_denied
                        DatabaseError.DatabaseRead.NOT_FOUND -> Res.string.firestore_not_found
                        DatabaseError.DatabaseRead.UNKNOWN -> Res.string.unknown_error_occurred_message
                    }

                    _state.update { it.copy(errorMessage = errorMessage) }

                }

                Result.Loading -> {}
            }


        }.invokeOnCompletion { _state.update { it.copy(isLoading = false) } }
    }

    private fun parseRecommendation(
        rawText: List<String>,
    ): Result<List<Recommendation>, ParsingError.ParsingRecommendations> {
        val regex = Regex(
            """\*\*Título:\*\*\s*(.+?)\r?\n\*\*Link:\*\*\s*(.+?)\r?\n\*\*Data:\*\*\s*(.+)"""
        )

        val list = mutableListOf<Recommendation>()

        rawText.forEach {

            val match = regex.find(it.trim())
            if (match == null) {
                return Result.Error(ParsingError.ParsingRecommendations.UNKNOWN)
            }

            val (titleText, linkText, dateText) = match.destructured

            val recommendation = Recommendation(titleText, linkText)
            list.add(recommendation)
        }

        return Result.Success(list)

    }

}