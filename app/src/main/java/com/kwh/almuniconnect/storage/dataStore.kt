package com.kwh.almuniconnect.storage

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val KEY_UID = stringPreferencesKey("uid")
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_EMAIL = stringPreferencesKey("email")
        val KEY_PHOTO = stringPreferencesKey("photo")
        val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
    }

    suspend fun saveUser(
        uid: String,
        name: String?,
        email: String?,
        photo: String?
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_UID] = uid
            prefs[KEY_NAME] = name ?: ""
            prefs[KEY_EMAIL] = email ?: ""
            prefs[KEY_PHOTO] = photo ?: ""
            prefs[KEY_LOGGED_IN] = true
        }
    }

    fun isLoggedIn(): Flow<Boolean> =
        context.dataStore.data.map {
            it[KEY_LOGGED_IN] ?: false
        }

    fun getUser(): Flow<UserLocalModel> =
        context.dataStore.data.map {
            UserLocalModel(
                uid = it[KEY_UID] ?: "",
                name = it[KEY_NAME] ?: "",
                email = it[KEY_EMAIL] ?: "",
                photo = it[KEY_PHOTO] ?: ""
            )
        }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}

data class UserLocalModel(
    val uid: String,
    val name: String,
    val email: String,
    val photo: String
)
