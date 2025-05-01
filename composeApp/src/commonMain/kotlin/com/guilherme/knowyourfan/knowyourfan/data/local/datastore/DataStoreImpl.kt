package com.guilherme.knowyourfan.knowyourfan.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.guilherme.knowyourfan.knowyourfan.domain.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DataStoreImpl(
    private val dataStore: DataStore<Preferences>,
) : DataStoreRepository {

    companion object {
        private val LAST_API_CALL_KEY = longPreferencesKey("last_api_call_time")
        private const val ONE_DAY_MS = 24 * 60 * 60 * 1000L
    }

    override suspend fun shouldCallApi(clock: Clock): Boolean {
        val prefs = dataStore.data.first()
        val lastCallMillis = prefs[LAST_API_CALL_KEY] ?: return true
        val nowMillis = clock.now().toEpochMilliseconds()

        return (nowMillis - lastCallMillis) >= ONE_DAY_MS
    }

    override suspend fun updateLastApiCallTime(clock: Clock) {
        dataStore.edit { prefs ->
            prefs[LAST_API_CALL_KEY] = clock.now().toEpochMilliseconds()
        }
    }

}