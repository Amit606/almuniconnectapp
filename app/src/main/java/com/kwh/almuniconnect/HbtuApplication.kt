package com.kwh.almuniconnect

import android.app.Application
import com.kwh.almuniconnect.analytics.AnalyticsManager
import com.kwh.almuniconnect.api.TokenManager
import com.kwh.almuniconnect.branding.RemoteConfigManager
import com.kwh.almuniconnect.storage.TokenDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
class HbtuApplication : Application() {

    lateinit var tokenManager: TokenManager
    lateinit var tokenDataStore: TokenDataStore

    // ✅ App-level CoroutineScope
    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onCreate() {
        super.onCreate()

        tokenManager = TokenManager()
        tokenDataStore = TokenDataStore(this)

        observeToken()
    }

    private fun observeToken() {
        applicationScope.launch {
            tokenDataStore.getTokens().collect { token ->
                tokenManager.accessToken = token.accessToken
            }
        }
    }
}
