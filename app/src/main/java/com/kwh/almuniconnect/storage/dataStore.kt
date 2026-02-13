package com.kwh.almuniconnect.storage

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val KEY_UID = stringPreferencesKey("uid")
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_EMAIL = stringPreferencesKey("email")
        val KEY_PHOTO = stringPreferencesKey("photo")
        val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")

        // 🔽 Profile Keys
        val KEY_MOBILE = stringPreferencesKey("mobile")
        val KEY_BRANCH = stringPreferencesKey("branch")
        val KEY_YEAR = stringPreferencesKey("year")
        val KEY_JOB = stringPreferencesKey("job")
        val KEY_LOCATION = stringPreferencesKey("location")
        val KEY_BIRTHDAY = stringPreferencesKey("birthday")
        val KEY_LINKEDIN = stringPreferencesKey("linkedin")
        val KEY_TOTAL_EXP = intPreferencesKey("total_exp")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_ACCESS_TOKEN_EXPIRY = stringPreferencesKey("access_token_expiry")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_REFRESH_TOKEN_EXPIRY = stringPreferencesKey("refresh_token_expiry")
        private val KEY_BRANCH_ID = intPreferencesKey("branch_id")

    }

    /* ---------------- LOGIN SAVE ---------------- */

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

    /* ---------------- PROFILE SAVE ---------------- */

    suspend fun saveProfile(profile: UserLocalModel) {

        context.dataStore.edit { prefs ->

            prefs[KEY_LOGGED_IN] = true

            prefs[KEY_USER_ID] = profile.userId
            prefs[KEY_NAME] = profile.name
            prefs[KEY_EMAIL] = profile.email
            prefs[KEY_PHOTO] = profile.photo
            prefs[KEY_MOBILE] = profile.mobile
            prefs[KEY_BRANCH] = profile.branch
            prefs[KEY_BRANCH_ID] = profile.branchId
            prefs[KEY_YEAR] = profile.year
            prefs[KEY_JOB] = profile.job
            prefs[KEY_LOCATION] = profile.location
            prefs[KEY_BIRTHDAY] = profile.birthday
            prefs[KEY_LINKEDIN] = profile.linkedin
            prefs[KEY_TOTAL_EXP] = profile.totalExp
            prefs[KEY_ACCESS_TOKEN] = profile.accessToken
            prefs[KEY_ACCESS_TOKEN_EXPIRY] = profile.accessTokenExpiry
            prefs[KEY_REFRESH_TOKEN] = profile.refreshToken
            prefs[KEY_REFRESH_TOKEN_EXPIRY] = profile.refreshTokenExpiry
        }
    }


    /* ---------------- READERS ---------------- */

    fun isLoggedIn(): Flow<Boolean> =
        context.dataStore.data.map {
            it[KEY_LOGGED_IN] ?: false
        }
    fun getUser(): Flow<UserLocalModel> =
        context.dataStore.data
            .map { prefs ->
                if (prefs[KEY_LOGGED_IN] != true) {
                    UserLocalModel()
                } else {
                    UserLocalModel(
                        name = prefs[KEY_NAME] ?: "",
                        email = prefs[KEY_EMAIL] ?: "",
                        photo = prefs[KEY_PHOTO] ?: "",
                        mobile = prefs[KEY_MOBILE] ?: "",
                        branch = prefs[KEY_BRANCH] ?: "",
                        year = prefs[KEY_YEAR] ?: "",
                        job = prefs[KEY_JOB] ?: "",
                        location = prefs[KEY_LOCATION] ?: "",
                        birthday = prefs[KEY_BIRTHDAY] ?: "",
                        linkedin = prefs[KEY_LINKEDIN] ?: "",
                        totalExp = prefs[KEY_TOTAL_EXP] ?: 0,

                    )
                }
            }
            .distinctUntilChanged()



    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
