package com.kwh.almuniconnect.nearby

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.UpdateLocationRequest
import com.kwh.almuniconnect.storage.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NearbyAlumniViewModel(
    private val context: Context,
    private val api: ApiService
) : ViewModel() {

    companion object {
        private const val TAG = "NearbyVM"
    }

    private val repository = NearbyRepository()
    private val tokenStore = TokenDataStore(context)

    var alumniList = mutableStateOf<List<NearAlumni>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    // 🔥 MAIN FUNCTION (Nearby + Location update together)
    fun loadNearby(lat: Double, lng: Double) {

        viewModelScope.launch {

            isLoading.value = true
            errorMessage.value = null

            try {
                Log.d(TAG, "Loading nearby for lat=$lat lng=$lng")

                // ✅ Step 1: Update location (non-blocking fail safe)
                updateLocation(lat, lng)

                // ✅ Step 2: Fetch nearby
                val result = repository.getNearby(lat, lng)

                alumniList.value = result

                Log.d(TAG, "Nearby count: ${result.size}")

                if (result.isEmpty()) {
                    errorMessage.value = "No Harcourtians found nearby."
                }

            } catch (e: Exception) {
                Log.e(TAG, "Nearby error: ${e.message}", e)
                errorMessage.value = "Failed to load data"
            } finally {
                isLoading.value = false
            }
        }
    }

    // 🔥 CLEANED VERSION (no external params)
    private suspend fun updateLocation(lat: Double, lng: Double) {

        try {
            val token = tokenStore.getAccessToken().first()
            Log.e(TAG, "Access token"+token)
            if (token.isNullOrEmpty()) {
                Log.e(TAG, "Access token missing → skipping location update")
                return
            }

            val response = api.updateCurrentLocation(
                authorization = "Bearer $token",
                request = UpdateLocationRequest(
                    latitude = lat.toString(),
                    longitude = lng.toString()
                )
            )

            if (response.isSuccessful) {

                val body = response.body()

                if (body?.success == true) {

                    Log.d(TAG, "✅ Location updated")
                    Log.d(TAG, "UserId: ${body.data?.userId}")
                    Log.d(TAG, "Lat: ${body.data?.latitude}")
                    Log.d(TAG, "Lng: ${body.data?.longitude}")
                    Log.d(TAG, "Time: ${body.data?.updatedAtUtc}")

                } else {
                    Log.e(TAG, "❌ API success=false: ${body?.message}")
                }

            } else {
                Log.e(TAG, "❌ HTTP Error: ${response.code()} ${response.errorBody()?.string()}")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Location update exception: ${e.message}", e)
        }
    }
}