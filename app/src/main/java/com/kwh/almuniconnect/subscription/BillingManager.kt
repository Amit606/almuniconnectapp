package com.kwh.almuniconnect.subscription

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams

class BillingManager(
    private val context: Context,
    private val onPremiumUnlocked: () -> Unit
) {

    private val billingClient = BillingClient.newBuilder(context)
        .setListener { _, purchases ->
            purchases?.forEach { handlePurchase(it) }
        }
        .enablePendingPurchases()
        .build()

    fun start() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {}
        })
    }

    fun buy(activity: Activity) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId("premium_lifetime")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()

        billingClient.queryProductDetailsAsync(params) { _, list ->
            val product = list.firstOrNull() ?: return@queryProductDetailsAsync

            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams
                            .newBuilder()
                            .setProductDetails(product)
                            .build()
                    )
                ).build()

            billingClient.launchBillingFlow(activity, flowParams)
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.products.contains("premium_lifetime")) {
            onPremiumUnlocked()
        }
    }

    private fun queryPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { _, list ->
            if (list.any { it.products.contains("premium_lifetime") }) {
                onPremiumUnlocked()
            }
        }
    }
}
