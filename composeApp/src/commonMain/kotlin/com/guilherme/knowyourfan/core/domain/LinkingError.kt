package com.guilherme.knowyourfan.core.domain

import com.guilherme.knowyourfan.domain.Error

sealed interface LinkingError: Error {
    enum class Twitter: Error {
        NULL_USER,
        NULL_CREDENTIALS,
        MFA_REQUIRED,
        NO_BROWSER_FOUND,
        ACCOUNT_ALREADY_LINKED,
        WEB_ERROR,
        NETWORK_ERROR,
        RATE_LIMITED,
        UNKNOWN
    }
}