package com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InlineData(
    @SerialName("mime_type") val mimeType: String,
    val data: String
)
