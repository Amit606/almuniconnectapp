package com.kwh.almuniconnect.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val repository = AlumniRepository()

    fun checkAlumniVerification(
        alumniId: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val isVerified = repository.isAlumniVerified(alumniId)
            onResult(isVerified)
        }
    }
}