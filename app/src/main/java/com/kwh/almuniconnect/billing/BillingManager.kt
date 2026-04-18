package com.kwh.almuniconnect.billing

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.android.billingclient.api.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



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

    private var userId: String = ""
    private var email: String = ""
    private var alumniID: String = ""

    private var productDetails: ProductDetails? = null

    private val billingClient: BillingClient =
        BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

    init {
        // 🔥 Load cached premium state
        _isPremium.value = prefs.getBoolean("is_premium", false)
    }

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

        billingClient.queryProductDetailsAsync(params) { result, list ->

            if (result.responseCode == BillingClient.BillingResponseCode.OK && list.isNotEmpty()) {

                productDetails = list.first()
                Log.d(TAG, "Product Loaded")
            } else {
                Log.e(TAG, "Product not found")
            }
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

            else -> {
                Log.e(TAG, "Purchase failed: ${billingResult.debugMessage}")
                PremiumAnalytics.logPurchaseFailed(context, billingResult.debugMessage)
            }
        }
    }

    // -------------------------------
    // Handle Purchase
    // -------------------------------
    private fun handlePurchase(purchase: Purchase) {

        // 🔥 IMPORTANT: validate product
        if (!purchase.products.contains(PRODUCT_ID)) return

        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

            if (!purchase.isAcknowledged) {

                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(params) { result ->

                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {

                        unlockPremium()
                        Log.d(TAG, "Purchase acknowledged")
                    }
                }

            } else {
                unlockPremium()
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

                Log.d(TAG, "Restoring purchases: ${purchases.size}")

                purchases.forEach { purchase ->

                    if (purchase.products.contains(PRODUCT_ID) &&
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        PremiumAnalytics.logRestore(context)
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

        prefs.edit().putBoolean("is_premium", true).apply()

        // 🔥 FIREBASE PURCHASE EVENT
        val firebase = FirebaseAnalytics.getInstance(context)

        val bundle = Bundle().apply {
            putDouble(FirebaseAnalytics.Param.VALUE, 199.0)
            putString(FirebaseAnalytics.Param.CURRENCY, "INR")
            putString(FirebaseAnalytics.Param.ITEM_ID, "alumni_premium_199")
        }
        firebase.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
        saveToFireStore(alumniID,email)
    }

fun saveToFireStore(alumniID:String,email: String) {

    val user = FirebaseAuth.getInstance().currentUser


    val userId = user?.uid   // ✅ always correct UID

    val db = FirebaseFirestore.getInstance()

    PremiumAnalytics.logPurchaseSuccess(context, userId.toString())

    val userData = hashMapOf(
        "userId" to userId,
        "alumniID" to alumniID,  // ✅ consistent field name
        "email" to email,
        "isPremium" to true,
        "productId" to "alumni_premium_199",
        "purchaseTime" to System.currentTimeMillis()
    )

    db.collection("subscriptions")
        .document(userId.toString())   // ✅ match rules
        .set(userData)
        .addOnSuccessListener {
            Log.d("Firestore", "Saved successfully")
        }
        .addOnFailureListener {
            Log.e("Firestore", "Error saving", it)
        }
}

    // -------------------------------
    // End Connection
    // -------------------------------
    fun endConnection() {
        billingClient.endConnection()
    }
}