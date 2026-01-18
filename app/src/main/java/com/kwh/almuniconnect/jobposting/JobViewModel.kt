package com.kwh.almuniconnect.jobposting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class JobState {
    object Loading : JobState()
    data class Success(val jobs: List<JobAPost>, val totalCount: Int) : JobState()
    data class Error(val message: String) : JobState()
}

class JobViewModel(
    private val repository: JobRepository
) : ViewModel() {

    private val _state = MutableStateFlow<JobState>(JobState.Loading)
    val state: StateFlow<JobState> = _state

    private var pageNumber = 1
    private val pageSize = 5
    private val jobList = mutableListOf<JobAPost>()

    fun loadJobs(reset: Boolean = false) {
        viewModelScope.launch {
            if (reset) {
                pageNumber = 1
                jobList.clear()
            }

            _state.value = JobState.Loading

            repository.getJobPosts(pageNumber, pageSize)
                .onSuccess {
                    jobList.addAll(it.items)
                    _state.value = JobState.Success(jobList, it.totalCount)
                    pageNumber++
                }
                .onFailure {
                    _state.value = JobState.Error(
                        it.message ?: "Failed to load jobs"
                    )
                }
        }
    }
}
