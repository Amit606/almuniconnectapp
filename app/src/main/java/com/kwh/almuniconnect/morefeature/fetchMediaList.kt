package com.kwh.almuniconnect.morefeature

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import org.json.JSONArray

fun fetchMediaList(onResult: (List<MediaItem>) -> Unit) {

    val remoteConfig = Firebase.remoteConfig

    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }

    remoteConfig.setConfigSettingsAsync(configSettings)

    // ✅ Default fallback (important)
    remoteConfig.setDefaultsAsync(
        mapOf("media_list" to "[]")
    )

    remoteConfig.fetchAndActivate()
        .addOnCompleteListener { task ->

            if (!task.isSuccessful) {
                onResult(emptyList())
                return@addOnCompleteListener
            }

            val json = remoteConfig.getString("media_list")

            try {
                val jsonArray = JSONArray(json)
                val list = mutableListOf<MediaItem>()

                for (i in 0 until jsonArray.length()) {

                    val obj = jsonArray.optJSONObject(i) ?: continue

                    val url = obj.optString("url", "")
                    val type = obj.optString("type", "photo")
                    val title = obj.optString("title", "")

                    // ✅ Skip invalid items
                    if (url.isBlank()) continue

                    list.add(
                        MediaItem(
                            url = url,
                            type = type,
                            title = title
                        )
                    )
                }

                onResult(list)

            } catch (e: Exception) {
                e.printStackTrace()
                onResult(emptyList()) // ✅ crash safe fallback
            }
        }
}