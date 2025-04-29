package com.guilherme.knowyourfan.core.domain

import com.guilherme.knowyourfan.domain.Error

sealed interface GeminiError : Error {

    enum class Gemini : Error {
        SERIALIZATION,
        REDIRECT_RESPONSE,
        CLIENT_REQUEST,
        SERVER_RESPONSE,
        UNRESOLVED_ADDRESS,
        IO,
        UNKNOWN
    }

}