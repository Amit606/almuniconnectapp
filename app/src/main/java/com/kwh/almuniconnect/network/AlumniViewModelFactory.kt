package com.kwh.almuniconnect.network

import AlumniViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlumniViewModelFactory(
    private val repository: AlumniRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlumniViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlumniViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}