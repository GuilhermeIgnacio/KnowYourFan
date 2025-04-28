package com.guilherme.knowyourfan.knowyourfan.data.remote.api.openai.model

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val parts: List<Part>
)
