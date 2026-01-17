package com.kwh.almuniconnect.evetns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventsViewModel : ViewModel() {

    private val api = NetworkClient.createService(ApiService::class.java)
    private val repository = EventsRepository(api)

    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val uiState: StateFlow<EventsUiState> = _uiState

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventsUiState.Loading
            try {
                val events = repository.fetchEvents()
                _uiState.value = EventsUiState.Success(events)
            } catch (e: Exception) {
                _uiState.value = EventsUiState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }
}


