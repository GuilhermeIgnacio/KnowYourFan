package com.guilherme.knowyourfan.core.domain

import com.guilherme.knowyourfan.domain.Error

sealed interface UserCheckError: Error {
    enum class User: Error {
        NULL_USER
    }
}