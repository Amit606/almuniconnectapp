package com.kwh.almuniconnect.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Locale

object AnalyticsManager {

    private lateinit var analytics: FirebaseAnalytics

    fun init(context: Context) {
        analytics = FirebaseAnalytics.getInstance(context)
    }

    fun logEvent(event: AnalyticsEvent) {
        val bundle = Bundle()

        event.params.forEach { (k, v) ->
            when (v) {
                is String -> bundle.putString(k, v)
                is Int -> bundle.putInt(k, v)
                is Long -> bundle.putLong(k, v)
                is Boolean -> bundle.putBoolean(k, v)
            }
        }

        analytics.logEvent(event.name, bundle)
    }
    fun logScreen(screenName: String, screenClass: String = "ComposeScreen") {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName.toLowerCase(Locale.ROOT))
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
}
