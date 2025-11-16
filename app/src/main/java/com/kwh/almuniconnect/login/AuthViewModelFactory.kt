package com.kwh.almuniconnect.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient


class AuthViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = AuthRepository(NetworkClient.createService(ApiService::class.java))
        @Suppress("UNCHECKED_CAST")
        return AuthViewModel(repo) as T
    }
}
