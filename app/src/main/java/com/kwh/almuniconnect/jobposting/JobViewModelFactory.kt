package com.kwh.almuniconnect.jobposting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class JobViewModelFactory(
    private val repository: JobRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JobViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
