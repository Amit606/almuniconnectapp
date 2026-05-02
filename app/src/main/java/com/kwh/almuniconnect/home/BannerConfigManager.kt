package com.kwh.almuniconnect.home

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import org.json.JSONArray

object BannerConfigManager {

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 0 // 1 hour
                }
            )
        }
    }

    private var cachedBanners: List<String>? = null

    suspend fun fetchBanners(): List<String> {

        if (cachedBanners != null) return cachedBanners!!

        return try {
            remoteConfig.fetchAndActivate().await()

            val json = remoteConfig.getString("home_banners")

            val list = JSONArray(json).let { array ->
                List(array.length()) { index ->
                    array.getString(index)
                }
            }

            cachedBanners = list
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}