package com.kwh.almuniconnect.branding

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kwh.almuniconnect.branding.ProductServiceItem



object RemoteConfigManager {

    // ✅ Lazy init (important)
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    private var isInitialized = false

    private fun ensureInit() {
        if (isInitialized) return

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        isInitialized = true
    }

    fun fetchProducts(onResult: (List<ProductServiceItem>) -> Unit) {

        ensureInit()

        // ✅ 1. Return cached data immediately (FAST UI)
        try {
            val cachedJson = remoteConfig.getString("product_services")

            if (cachedJson.isNotEmpty()) {
                val type = object :
                    TypeToken<List<ProductServiceItem>>() {}.type

                val cachedList: List<ProductServiceItem> =
                    Gson().fromJson(cachedJson, type) ?: emptyList()

                onResult(
                    cachedList.filter { it.isActive }
                        .sortedBy { it.srNo }
                )
            }
        } catch (e: Exception) {
            Log.e("RemoteConfig", "Cache parse error ${e.message}")
        }

        // ✅ 2. Fetch latest in background
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    try {
                        val json = remoteConfig.getString("product_services")

                        val type = object :
                            TypeToken<List<ProductServiceItem>>() {}.type

                        val list: List<ProductServiceItem> =
                            Gson().fromJson(json, type) ?: emptyList()

                        onResult(
                            list.filter { it.isActive }
                                .sortedBy { it.srNo }
                        )

                    } catch (e: Exception) {
                        Log.e("RemoteConfig", "Parse error ${e.message}")
                    }

                } else {
                    Log.e("RemoteConfig", "Fetch failed", task.exception)
                }
            }
    }
}