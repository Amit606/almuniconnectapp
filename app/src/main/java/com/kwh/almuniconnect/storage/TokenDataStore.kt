package com.kwh.almuniconnect.storage
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.kwh.almuniconnect.model.TokenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore by preferencesDataStore(name = "auth_preferences")

class TokenDataStore(private val context: Context) {


    private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    private val ACCESS_TOKEN_EXPIRY = longPreferencesKey("access_token_expiry")
    private val REFRESH_TOKEN_EXPIRY = longPreferencesKey("refresh_token_expiry")

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
        accessTokenExpiry: Long,
        refreshTokenExpiry: Long
    ) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[ACCESS_TOKEN_EXPIRY] = accessTokenExpiry
            preferences[REFRESH_TOKEN_EXPIRY] = refreshTokenExpiry
        }
    }

    fun getTokens(): Flow<TokenModel> {
        return context.dataStore.data.map { prefs ->
            TokenModel(
                accessToken = prefs[ACCESS_TOKEN],
                refreshToken = prefs[REFRESH_TOKEN],
                accessExpiry = prefs[ACCESS_TOKEN_EXPIRY],
                refreshExpiry = prefs[REFRESH_TOKEN_EXPIRY]
            )
        }
    }
    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }
    }

    fun getAccessTokenExpiry(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_EXPIRY]
        }
    }
    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
    fun isAccessTokenExpired(expiry: Long?): Boolean {
        return expiry == null || System.currentTimeMillis() > expiry
    }

    fun getRefreshTokenExpiry(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_EXPIRY]
        }
    }
}