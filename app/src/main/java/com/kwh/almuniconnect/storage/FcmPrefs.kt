package com.kwh.almuniconnect.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object FcmPrefs {

    private val Context.dataStore by preferencesDataStore("fcm_store")

    private val FCM_TOKEN = stringPreferencesKey("fcm_token")

    suspend fun saveToken(context: Context, token: String) {
        context.dataStore.edit { prefs ->
            prefs[FCM_TOKEN] = token
        }
    }

    suspend fun getToken(context: Context): String? {
        return context.dataStore.data.first()[FCM_TOKEN]
    }
}
