package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase.model

data class UserInterests(
    val events: List<String> = emptyList(),
    val interestGames: List<String> = emptyList(),
    val purchases: List<String> = emptyList()
)
