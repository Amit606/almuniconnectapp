package com.kwh.almuniconnect.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.billing.BillingManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BillingViewModel(application: Application) :
    AndroidViewModel(application) {

    private val billingManager = BillingManager(application)

    val isPremium = billingManager.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isLoading = billingManager.isLoading
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // 🔥 ADD THIS (IMPORTANT)
    private val _purchaseSuccess = MutableSharedFlow<Unit>()
    val purchaseSuccess = _purchaseSuccess

    fun startBilling() {
        billingManager.startConnection()
    }

    fun buy(activity: Activity, email: String, userID: String) {
        billingManager.setUser(userID, email)
        billingManager.launchPurchase(activity)
    }

    fun restore() {
        billingManager.restorePurchase()
    }

    init {
        observePremium()
    }

    // 🔥 MAIN FIX
    private fun observePremium() {
        viewModelScope.launch {
            billingManager.isPremium.collect { premium ->
                if (premium) {
                    _purchaseSuccess.emit(Unit)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.endConnection()
    }
}