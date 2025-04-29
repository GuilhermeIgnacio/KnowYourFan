package com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Part(
    @SerialName("inline_data") val inlineData: InlineData? = null,
    val text: String? = null
)
