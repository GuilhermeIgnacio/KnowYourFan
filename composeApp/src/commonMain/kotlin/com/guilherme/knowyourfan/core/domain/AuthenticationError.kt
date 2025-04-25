package com.guilherme.knowyourfan.domain

sealed interface AuthenticationError: Error {
    enum class Authentication : Error {
        GET_CREDENTIAL_EXCEPTION,
        GET_CREDENTIAL_UNKNOWN,
        GET_CREDENTIAL_CANCELLATION,
        GET_CREDENTIAL_INTERRUPTED,
        FIREBASE_AUTH_INVALID_USER,
        FIREBASE_AUTH_INVALID_CREDENTIALS,
        FIREBASE_AUTH_USER_COLLISION,
        UNKNOWN
    }

    enum class Network : Error {
        UNKNOWN
    }

}