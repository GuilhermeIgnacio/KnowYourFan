package com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val candidates: List<Candidate>
)