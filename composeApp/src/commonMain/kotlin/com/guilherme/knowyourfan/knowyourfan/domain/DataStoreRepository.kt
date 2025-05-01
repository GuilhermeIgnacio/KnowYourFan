package com.guilherme.knowyourfan.knowyourfan.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface DataStoreRepository {

    suspend fun shouldCallApi(clock: Clock = Clock.System): Boolean
    suspend fun updateLastApiCallTime(clock: Clock = Clock.System)

}