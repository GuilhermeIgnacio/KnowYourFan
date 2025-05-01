package com.guilherme.knowyourfan.core.domain

import com.guilherme.knowyourfan.domain.Error

sealed interface GeminiError : Error {

    enum class Gemini : Error {
        SERIALIZATION,
        REDIRECT_RESPONSE,
        CLIENT_REQUEST,
        SERVER_RESPONSE,
        UNRESOLVED_ADDRESS,
        UNKNOWN,
        IO
    }

    enum class Recommendations : Error {
        UNKNOWN,
        BAD_REQUEST,
        UNAUTHORIZED,
        SERVER_ERROR,
        SERVICE_UNAVAILABLE
    }

}