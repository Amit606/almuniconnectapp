package com.kwh.almuniconnect.storage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserSession {

    private val Context.dataStore by preferencesDataStore("user_session")

    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

    suspend fun saveLogin(context: Context) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = true
        }
    }

    suspend fun logout(context: Context) {
        context.dataStore.edit {
            it[IS_LOGGED_IN] = false
        }
    }

    fun isLoggedIn(context: Context): Flow<Boolean> {
        return context.dataStore.data.map {
            it[IS_LOGGED_IN] ?: false
        }
    }
}
