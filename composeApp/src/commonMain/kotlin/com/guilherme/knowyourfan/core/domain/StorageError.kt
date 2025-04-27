package com.guilherme.knowyourfan.core.domain

import com.guilherme.knowyourfan.domain.Error

interface StorageError: Error {
    enum class Storage : Error {
        UNKNOWN
    }
}