package com.kwh.almuniconnect.billing

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object PremiumAnalytics {

    fun logPaywallViewed(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent("paywall_viewed", null)
    }

    fun logPurchaseClick(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent("purchase_click", null)
    }

    fun logPurchaseSuccess(context: Context, userId: String) {
        val bundle = Bundle().apply {
            putDouble(FirebaseAnalytics.Param.VALUE, 199.0)
            putString("user_id", userId)
            putString("product_id", "alumni_premium_199")
        }
        FirebaseAnalytics.getInstance(context).logEvent("purchase_success", bundle)
    }

    fun logPurchaseFailed(context: Context, reason: String) {
        val bundle = Bundle().apply {
            putString("reason", reason)
        }
        FirebaseAnalytics.getInstance(context).logEvent("purchase_failed", bundle)
    }

    fun logRestore(context: Context) {
        FirebaseAnalytics.getInstance(context).logEvent("premium_restored", null)
    }
}