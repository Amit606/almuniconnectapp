package com.kwh.almuniconnect.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.billing.BillingManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class BillingViewModel(application: Application) :
    AndroidViewModel(application) {

    private val billingManager = BillingManager(application)

    val isPremium = billingManager.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isLoading = billingManager.isLoading
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun startBilling() {
        billingManager.startConnection()
    }

    fun buy(activity: Activity) {
        billingManager.launchPurchase(activity)
    }

    fun restore() {
        billingManager.restorePurchase()
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.endConnection()
    }
}
