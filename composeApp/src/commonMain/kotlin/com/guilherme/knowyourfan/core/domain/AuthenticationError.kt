package com.guilherme.knowyourfan.domain

sealed interface AuthenticationError: Error {
    enum class SignUp : Error {
        GET_CREDENTIAL_EXCEPTION,
        GET_CREDENTIAL_UNKNOWN,
        GET_CREDENTIAL_CANCELLATION,
        GET_CREDENTIAL_INTERRUPTED,
        FIREBASE_AUTH_INVALID_USER,
        FIREBASE_AUTH_INVALID_CREDENTIALS,
        FIREBASE_AUTH_USER_COLLISION,
        FIREBASE_AUTH_WEAK_PASSWORD,
        UNKNOWN
    }

    enum class SignIn: Error {
        USER_NOT_FOUND,
        INVALID_CREDENTIALS,
        TOO_MANY_REQUESTS,
        UNKNOWN
    }

    enum class Network : Error {
        UNKNOWN
    }

}