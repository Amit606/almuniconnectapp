package com.kwh.almuniconnect.evetns

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.Context
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

//class EventsViewModel() : ViewModel() {
//    private val api = NetworkClient.createService(ApiService::class.java)
//    private val repository = EventsRepository(api)
//
//    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
//    val uiState: StateFlow<EventsUiState> = _uiState
//
//    fun loadEvents() {
//        viewModelScope.launch {
//            _uiState.value = EventsUiState.Loading
//            try {
//                val events = repository.fetchEvents()
//                _uiState.value = EventsUiState.Success(events)
//            } catch (e: Exception) {
//                _uiState.value = EventsUiState.Error(
//                    e.message ?: "Something went wrong"
//                )
//            }
//        }
//    }
//}


class EventsViewModel() : ViewModel() {

    private val api = NetworkClient.createService(ApiService::class.java)
    private val repository = EventsRepository(api)

    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val uiState: StateFlow<EventsUiState> = _uiState

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventsUiState.Loading
            try {
                val events = repository.fetchEvents()

                val now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"))

                val upcomingEvents = events.filter { event ->
                    try {
                        val endTime = LocalDateTime.parse(event.endAt)
                        endTime.isAfter(now)
                    } catch (e: Exception) {
                        false
                    }
                }

                _uiState.value = EventsUiState.Success(upcomingEvents)

            } catch (e: Exception) {
                _uiState.value =
                    EventsUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}