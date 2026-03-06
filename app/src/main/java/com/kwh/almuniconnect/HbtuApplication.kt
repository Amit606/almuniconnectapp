package com.kwh.almuniconnect

import android.app.Application
import com.kwh.almuniconnect.analytics.AnalyticsManager
import com.kwh.almuniconnect.branding.RemoteConfigManager

class HbtuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AnalyticsManager.init(this)
        RemoteConfigManager.init()


    }
}