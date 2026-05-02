package com.kwh.almuniconnect.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.kwh.almuniconnect.api.BatchCount

class AlumniBatchViewModel(
    private val repository: AlumniRepository
) : ViewModel() {

    var batchList by mutableStateOf<List<BatchCount>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set


    fun loadAlumniBatchCount(branchId: Int) {

        viewModelScope.launch {

            isLoading = true
            errorMessage = null

            val result = repository.getAlumniBatchCount(branchId)

            result.onSuccess {

                batchList = it

            }.onFailure {

                errorMessage = it.message ?: "Something went wrong"

            }

            isLoading = false
        }
    }
}