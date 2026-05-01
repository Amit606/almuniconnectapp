package com.kwh.almuniconnect.almunipost

import android.annotation.SuppressLint
import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.model.AlumniStory
import kotlinx.coroutines.tasks.await

object SuccessRepository {

    @SuppressLint("StaticFieldLeak")
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val gson = Gson()

    suspend fun fetchAlumni(context: Context): List<AlumniStory> {

        return try {

            // 🔥 Set minimum fetch interval (production safe)
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }

            remoteConfig.setConfigSettingsAsync(configSettings)

            // Try fetch
            remoteConfig.fetchAndActivate().await()

            val json = remoteConfig.getString("alumni_feed_json")

            if (json.isEmpty()) {
                loadFallbackFromLocal(context)
            } else {
                parseJson(json) ?: loadFallbackFromLocal(context)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            loadFallbackFromLocal(context)   // 🔥 Always fallback
        }
    }

    private fun parseJson(json: String): List<AlumniStory>? {
        return try {
            val type = object : TypeToken<List<AlumniStory>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            null
        }
    }

    private fun loadFallbackFromLocal(context: Context): List<AlumniStory> {
        return try {
            val inputStream = context.resources.openRawResource(R.raw.alumni_fallback)
            val json = inputStream.bufferedReader().use { it.readText() }
            parseJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()   // ultimate safety
        }
    }
}