package com.kwh.almuniconnect

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kwh.almuniconnect.analytics.AnalyticsManager

class HbtuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AnalyticsManager.init(this)

    }
}