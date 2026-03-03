package com.kwh.almuniconnect.api

import com.kwh.almuniconnect.api.NetworkClient.logging
import com.kwh.almuniconnect.storage.TokenDataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton Retrofit client — handles all network calls for GET/POST.
 */
object NetworkClient {

    private const val BASE_URL = "https://hbtu-alumni-api.azurewebsites.net/api/v1/"  // 🔁 Replace with your backend base URL

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
        }


    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}
