package com.guilherme.knowyourfan.core.domain

import com.guilherme.knowyourfan.domain.Error

sealed interface ParsingError: Error {

    enum class ParsingRecommendations: Error {
        UNKNOWN
    }

}