package com.kwh.almuniconnect.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kwh.almuniconnect.login.AuthRepository
import com.kwh.almuniconnect.storage.TokenDataStore

class ProfileViewModelFactory(
    private val repository: AuthRepository,
    private val tokenDataStore: TokenDataStore

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository,tokenDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
