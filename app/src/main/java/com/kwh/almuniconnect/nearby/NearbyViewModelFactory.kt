package com.kwh.almuniconnect.nearby

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient

class NearbyViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
         val api = NetworkClient.createService(ApiService::class.java)

        return NearbyAlumniViewModel(context, api) as T
    }
}