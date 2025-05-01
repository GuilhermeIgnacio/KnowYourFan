package com.guilherme.knowyourfan.knowyourfan.domain

interface RecommendationRepository {

    fun fetchData() : List<Recommendation>
    suspend fun cacheData(title: String, link: String)

}

data class Recommendation(
    val title: String,
    val link: String
)