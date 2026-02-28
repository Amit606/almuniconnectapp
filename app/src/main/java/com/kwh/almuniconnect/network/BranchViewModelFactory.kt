package com.kwh.almuniconnect.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BranchViewModelFactory(
    private val repository: BranchRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BranchViewModel::class.java)) {
            return BranchViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}