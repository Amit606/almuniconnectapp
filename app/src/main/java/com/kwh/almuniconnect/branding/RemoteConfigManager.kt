package com.kwh.almuniconnect.branding

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kwh.almuniconnect.branding.ProductServiceItem

object RemoteConfigManager {

    @SuppressLint("StaticFieldLeak")
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    fun init() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun fetchProducts(onResult: (List<ProductServiceItem>) -> Unit) {

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val json = remoteConfig.getString("product_services")

                    try {

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
                        onResult(emptyList())
                    }

                } else {

                    Log.e("RemoteConfig", "Fetch failed", task.exception)
                    onResult(emptyList())
                }
            }
    }

}