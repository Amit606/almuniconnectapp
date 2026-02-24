package com.kwh.almuniconnect.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var banners by mutableStateOf<List<String>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            banners = BannerConfigManager.fetchBanners()
            isLoading = false
        }
    }
}