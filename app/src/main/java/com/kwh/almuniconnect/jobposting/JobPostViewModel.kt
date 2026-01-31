package com.kwh.almuniconnect.jobposting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.JobPostRequest
import kotlinx.coroutines.launch

class JobPostViewModel : ViewModel() {

    var loading by mutableStateOf(false)
        private set

    var success by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun postJob(request: JobPostRequest) {
        viewModelScope.launch {
            loading = true
            error = null
            success = false

            try {
                val response = JobPostService.api.createJobPost(request)
                if (response.isSuccessful) {
                    success = true
                } else {
                    error = "Error ${response.code()}"
                }
            } catch (e: Exception) {
                error = e.localizedMessage
            } finally {
                loading = false
            }
        }
    }
}
