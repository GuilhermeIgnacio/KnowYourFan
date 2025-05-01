package com.guilherme.knowyourfan.knowyourfan.data.remote.api.gemini.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Candidate(
    val content: Content? = null,
    @SerialName("groundingMetadata") val grounding: GroundingMetaData? = null,
)

@Serializable
data class GroundingMetaData(
    @SerialName("groundingChunks")
    val chunks: List<GroundingChunk> = emptyList(),
    @SerialName("groundingSupports")
    val supports: List<GroundingSupport> = emptyList(),
)

@Serializable
data class GroundingChunk(
    val web: WebInfo,
)

@Serializable
data class GroundingSupport(
    val segment: Segment,
)

@Serializable
data class WebInfo(
    val uri: String,
    val title: String,
)

@Serializable
data class Segment(
    val text: String,
)
