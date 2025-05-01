package com.guilherme.knowyourfan.knowyourfan.data.local

import app.cash.sqldelight.db.SqlDriver
import com.guilherme.Database
import com.guilherme.knowyourfan.knowyourfan.domain.Recommendation
import com.guilherme.knowyourfan.knowyourfan.domain.RecommendationRepository

class RecommendationImpl(
    private val driverFactory: DriverFactory
): RecommendationRepository {

    val driver = driverFactory.createDriver()
    val database = Database(driver)
    val recommendationQueries = database.recommendationQueries

    override fun fetchData(): List<Recommendation> {
        return recommendationQueries.selectAll().executeAsList().map {
            Recommendation(
                title = it.title!!,
                link = it.link!!
            )
        }
    }

    override suspend fun cacheData(title: String, link: String) {
        recommendationQueries.insert(title,link)
    }
}