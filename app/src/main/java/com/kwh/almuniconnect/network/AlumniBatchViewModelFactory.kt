package com.kwh.almuniconnect.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlumniBatchViewModelFactory(
    private val repository: AlumniRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AlumniBatchViewModel::class.java)) {

            return AlumniBatchViewModel(repository) as T

        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}