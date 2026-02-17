package com.kwh.almuniconnect.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BillingManager(
    private val context: Context
) : PurchasesUpdatedListener {

    companion object {
        private const val PRODUCT_ID = "alumni_premium_199"
        private const val TAG = "BillingManager"
    }

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var productDetails: ProductDetails? = null

    private val billingClient: BillingClient =
        BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

    // -------------------------------
    // Start Billing Connection
    // -------------------------------
    fun startConnection(onConnected: (() -> Unit)? = null) {
        if (billingClient.isReady) {
            onConnected?.invoke()
            return
        }

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing Connected")
                    queryProductDetails()
                    restorePurchase()
                    onConnected?.invoke()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Billing Disconnected")
            }
        })
    }

    // -------------------------------
    // Query Product Details
    // -------------------------------
    private fun queryProductDetails() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(PRODUCT_ID)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, list ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && list.isNotEmpty()) {
                productDetails = list.first()
                Log.d(TAG, "Product Loaded")
            }
        }
    }

    // -------------------------------
    // Launch Purchase Flow
    // -------------------------------
    fun launchPurchase(activity: Activity) {
        if (_isPremium.value) {
            Log.d(TAG, "User already premium")
            return
        }

        val details = productDetails ?: return

        _isLoading.value = true

        val productDetailsParams =
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details)
                .build()

        val billingFlowParams =
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
    }

    // -------------------------------
    // Purchase Callback
    // -------------------------------
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        _isLoading.value = false

        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach { handlePurchase(it) }
        } else {
            Log.d(TAG, "Purchase Failed: ${billingResult.debugMessage}")
        }
    }

    // -------------------------------
    // Handle Purchase
    // -------------------------------
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

            if (!purchase.isAcknowledged) {
                val acknowledgeParams =
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                billingClient.acknowledgePurchase(acknowledgeParams) { result ->
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        _isPremium.value = true
                        Log.d(TAG, "Purchase Acknowledged")
                    }
                }
            } else {
                _isPremium.value = true
            }
        }
    }

    // -------------------------------
    // Restore Purchase
    // -------------------------------
    fun restorePurchase() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { result, purchases ->

            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach { purchase ->
                    if (purchase.products.contains(PRODUCT_ID) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        _isPremium.value = true
                    }
                }
            }
        }
    }

    fun endConnection() {
        billingClient.endConnection()
    }
}
