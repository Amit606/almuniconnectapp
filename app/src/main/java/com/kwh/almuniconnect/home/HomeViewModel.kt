package com.kwh.almuniconnect.home

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.RefreshTokenRequest
import com.kwh.almuniconnect.storage.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.OffsetDateTime


@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel(
    private val context: Context,
    private val authApi: ApiService
) : ViewModel() {

    companion object {
        private const val TAG = "HomeVM"
    }

    private val tokenStore = TokenDataStore(context)

    var banners by mutableStateOf<List<String>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            Log.d(TAG, "HomeViewModel init started")

            // ✅ Step 1: Refresh token
            refreshTokenIfNeeded()

            // ✅ Step 2: Load banners
            banners = BannerConfigManager.fetchBanners()
            Log.d(TAG, "Banners loaded: ${banners.size}")

            isLoading = false
            Log.d(TAG, "Home loading completed")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun refreshTokenIfNeeded() {

        val refreshToken = tokenStore.getRefreshToken().first()
        val accessToken = tokenStore.getAccessToken().first()
        val expiry = tokenStore.getAccessTokenExpiry().first()

        if (refreshToken.isNullOrEmpty()) {
            Log.w(TAG, "Refresh token missing → skipping refresh")
            return
        }

        if (accessToken.isNullOrEmpty()) {
            Log.w(TAG, "Access token missing → skipping refresh")
            return
        }

        if (!isTokenExpired(expiry)) {

            Log.d(TAG, "Token expired → calling refresh API"+refreshToken)

            try {
                val response = authApi.refreshToken(
                    token = "Bearer $accessToken",
                    request = RefreshTokenRequest(refreshToken)
                )

                if (response.isSuccessful) {

                    val body = response.body() ?: return
                    val data = body.data

                    val expiryMillis = Instant.parse(data.accessTokenExpiry)
                        .toEpochMilli()

                    tokenStore.saveTokens(
                        accessToken = data.accessToken,
                        refreshToken = data.refreshToken,
                        accessTokenExpiry = expiryMillis.toString(),
                        refreshTokenExpiry = data.refreshTokenExpiry
                    )

                    Log.d(TAG, "Tokens updated successfully"+data.accessToken)

                } else {
                    Log.e(TAG, "Refresh failed: ${response.code()} ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Refresh exception: ${e.message}", e)
            }

        } else {
            Log.d(TAG, "Token still valid → no refresh needed")
        }
    }


    private fun isTokenExpired(expiry: String?): Boolean {
        if (expiry.isNullOrEmpty()) return true

        return try {
            val expiryTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                OffsetDateTime.parse(expiry).toInstant().toEpochMilli()
            } else {
                Log.e("HomeVM", "Expiry parsing not supported on SDK < 26")
                return true
            }
            val isExpired = System.currentTimeMillis() >= expiryTime

            Log.d("HomeVM", "Parsed ExpiryTime: $expiryTime, Expired: $isExpired")

            isExpired
        } catch (e: Exception) {
            Log.e("HomeVM", "Expiry parse failed", e)
            true
        }
    }
}