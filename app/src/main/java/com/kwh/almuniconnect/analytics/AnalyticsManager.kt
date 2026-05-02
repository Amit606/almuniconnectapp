package com.kwh.almuniconnect.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Locale

object AnalyticsManager {

    private var analytics: FirebaseAnalytics? = null



    private fun getAnalytics(context: Context): FirebaseAnalytics {
        if (analytics == null) {
            analytics = FirebaseAnalytics.getInstance(context)
        }
        return analytics!!
    }

    fun logEvent(context: Context, event: AnalyticsEvent) {
        val bundle = Bundle()

        event.params.forEach { (k, v) ->
            when (v) {
                is String -> bundle.putString(k, v)
                is Int -> bundle.putInt(k, v)
                is Long -> bundle.putLong(k, v)
                is Boolean -> bundle.putBoolean(k, v)
            }
        }

        getAnalytics(context).logEvent(event.name, bundle)
    }

    fun logScreen(
        context: Context,
        screenName: String,
        screenClass: String = "ComposeScreen"
    ) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName.lowercase())
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }

        getAnalytics(context).logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            bundle
        )
    }
}
