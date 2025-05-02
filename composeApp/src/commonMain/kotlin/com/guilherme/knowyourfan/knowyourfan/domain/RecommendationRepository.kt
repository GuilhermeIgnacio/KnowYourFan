package com.guilherme.knowyourfan.knowyourfan.domain

import kotlinx.coroutines.flow.Flow

interface RecommendationRepository {

    fun fetchData() : Flow<List<Recommendation>>
    suspend fun cacheData(recommendations: List<Recommendation>)

}

data class Recommendation(
    val title: String,
    val link: String,
)