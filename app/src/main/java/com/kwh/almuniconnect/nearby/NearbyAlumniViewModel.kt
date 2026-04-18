package com.kwh.almuniconnect.nearby

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NearbyAlumniViewModel : ViewModel() {

    private val repository = NearbyRepository()

    var alumniList = mutableStateOf<List<NearAlumni>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun loadNearby(lat: Double, lng: Double) {

        viewModelScope.launch {

            isLoading.value = true
            errorMessage.value = null

            try {
                val result = repository.getNearby(lat, lng)

                alumniList.value = result

                if (result.isEmpty()) {
                    errorMessage.value = "No Harcourtians found nearby."
                }

            } catch (e: Exception) {
                errorMessage.value = "Failed to load data"
            } finally {
                isLoading.value = false
            }
        }
    }
}