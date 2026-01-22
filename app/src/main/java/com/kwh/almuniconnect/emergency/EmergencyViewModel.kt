package com.kwh.almuniconnect.emergency

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EmergencyViewModel : ViewModel() {

    var loading by mutableStateOf(false)
        private set

    fun submitEmergency(
        type: String,
        description: String,
        amount: String
    ) {
        loading = true

        // TODO: API call
        // onSuccess → loading = false
    }

    fun donate(amount: Int) {
        // Razorpay integration
    }
}
