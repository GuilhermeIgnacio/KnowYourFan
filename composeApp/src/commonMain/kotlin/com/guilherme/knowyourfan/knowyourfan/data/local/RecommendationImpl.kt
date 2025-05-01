package com.guilherme.knowyourfan.knowyourfan.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import com.guilherme.Database
import com.guilherme.knowyourfan.knowyourfan.domain.Recommendation
import com.guilherme.knowyourfan.knowyourfan.domain.RecommendationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RecommendationImpl(
    private val driverFactory: DriverFactory,
) : RecommendationRepository {

    val driver = driverFactory.createDriver()
    val database = Database(driver)
    val recommendationQueries = database.recommendationQueries

    override fun fetchData(): Flow<List<Recommendation>> {
        return recommendationQueries
            .selectAll { id, title, link ->
                Recommendation(title = title, link = link)
            }
            .asFlow()
            .mapToList(Dispatchers.IO)

    }

    override suspend fun cacheData(recommendations: List<Recommendation>) {

        recommendationQueries.transaction {
            recommendations.forEach { recommendation ->
                recommendationQueries.insert(
                    recommendation.title,
                    recommendation.link
                )
            }
        }

    }
}