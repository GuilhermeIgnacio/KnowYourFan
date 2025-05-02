package com.guilherme.knowyourfan.core.domain

import com.guilherme.knowyourfan.domain.Error

sealed interface DatabaseError : Error {
    enum class DatabaseRead : Error {
        NULL_USER,
        PERMISSION_DENIED,
        NOT_FOUND,
        UNKNOWN
    }
}