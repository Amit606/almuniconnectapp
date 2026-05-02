package com.kwh.almuniconnect.billing

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.android.billingclient.api.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.core.content.edit

class BillingManager(
    private val context: Context
) : PurchasesUpdatedListener {

    companion object {
        private const val PRODUCT_ID = "alumni_premium_199"
        private const val TAG = "BillingManager"
    }

    private val prefs =
        context.getSharedPreferences("billing_prefs", Context.MODE_PRIVATE)

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var email: String = ""
    private var alumniID: String = ""

    private var productDetails: ProductDetails? = null

    // ✅ FIXED BillingClient (v8 compliant)
    private val billingClient: BillingClient =
        BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()   // ✅ REQUIRED
                    .build()
            )
            .build()

    init {
        _isPremium.value = prefs.getBoolean("is_premium", false)
    }

    // -------------------------------
    // Start Connection
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
    // Query Product
    // -------------------------------
    private fun queryProductDetails() {

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(params) { result, productDetailsList ->

            // 🔍 Debug logs (VERY IMPORTANT)
            Log.d(TAG, "ResponseCode: ${result.responseCode}")
            Log.d(TAG, "DebugMessage: ${result.debugMessage}")
            Log.d(TAG, "ProductListSize: ${productDetailsList.unfetchedProductList}")

            // ❌ If billing itself failed
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                Log.e(TAG, "Billing error: ${result.debugMessage}")
                productDetails = null
                return@queryProductDetailsAsync
            }

            // ❌ If list is empty (MOST COMMON ISSUE)


            // ✅ Safe access

        }
    }

    fun setUser(userId: String, email: String) {
        this.alumniID = userId
        this.email = email
    }

    // -------------------------------
    // Launch Purchase
    // -------------------------------
    fun launchPurchase(activity: Activity) {

        if (_isPremium.value) {
            Log.d(TAG, "Already premium")
            return
        }

        if (productDetails == null) {
            Log.e(TAG, "Product not loaded yet")
            return
        }

        _isLoading.value = true

        val productParams =
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails!!)
                .build()

        val flowParams =
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productParams))
                .build()

        billingClient.launchBillingFlow(activity, flowParams)
    }

    // -------------------------------
    // Purchase Callback
    // -------------------------------
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {

        _isLoading.value = false

        when (billingResult.responseCode) {

            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { handlePurchase(it) }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "User cancelled purchase")
            }

            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Log.d(TAG, "Item already owned → restoring")
                restorePurchase()
            }

            else -> {
                Log.e(TAG, "Purchase failed: ${billingResult.debugMessage}")
            }
        }
    }

    // -------------------------------
    // Handle Purchase
    // -------------------------------
    private fun handlePurchase(purchase: Purchase) {

        if (!purchase.products.contains(PRODUCT_ID)) return

        when (purchase.purchaseState) {

            Purchase.PurchaseState.PURCHASED -> {

                savePurchaseToFirestore(
                    purchase.purchaseToken,
                    alumniID,
                    email
                )

                if (!purchase.isAcknowledged) {

                    val params = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()

                    billingClient.acknowledgePurchase(params) { result ->

                        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                            unlockPremium()
                        }
                    }

                } else {
                    unlockPremium()
                }
            }

            Purchase.PurchaseState.PENDING -> {
                Log.d(TAG, "Purchase Pending")
            }

            else -> {
                Log.d(TAG, "Purchase state unknown")
            }
        }
    }

    // -------------------------------
    // Restore Purchase
    // -------------------------------
    fun restorePurchase() {

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { result, purchases ->

            if (result.responseCode == BillingClient.BillingResponseCode.OK) {

                purchases.forEach { purchase ->

                    if (purchase.products.contains(PRODUCT_ID) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        unlockPremium()
                    }
                }
            }
        }
    }

    // -------------------------------
    // Unlock Premium
    // -------------------------------
    private fun unlockPremium() {

        _isPremium.value = true

        prefs.edit {
            putBoolean("is_premium", true)
        }

        val firebase = FirebaseAnalytics.getInstance(context)

        val bundle = Bundle().apply {
            putDouble(FirebaseAnalytics.Param.VALUE, 199.0)
            putString(FirebaseAnalytics.Param.CURRENCY, "INR")
            putString(FirebaseAnalytics.Param.ITEM_ID, PRODUCT_ID)
        }

        firebase.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
    }

    // -------------------------------
    // Firestore Save
    // -------------------------------
    private fun savePurchaseToFirestore(
        purchaseToken: String,
        alumniID: String,
        email: String
    ) {

        val user = FirebaseAuth.getInstance().currentUser ?: return

        val db = FirebaseFirestore.getInstance()

        val userData = hashMapOf(
            "userId" to user.uid,
            "alumniID" to alumniID,
            "email" to email,
            "isPremium" to true,
            "productId" to PRODUCT_ID,
            "purchaseToken" to purchaseToken,
            "purchaseTime" to System.currentTimeMillis()
        )

        db.collection("subscriptions")
            .document(user.uid)
            .set(userData)
    }

    // -------------------------------
    // End Connection
    // -------------------------------
    fun endConnection() {
        billingClient.endConnection()
    }
}