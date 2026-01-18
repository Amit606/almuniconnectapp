package com.kwh.almuniconnect.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NetworkViewModel : ViewModel() {

    private val api = NetworkClient.createService(ApiService::class.java)
    private val repository = NetworkRepository(api)

    private val _alumniList = MutableStateFlow<List<Alumni>>(emptyList())
    val alumniList: StateFlow<List<Alumni>> = _alumniList

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadAlumniList() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getAlumniList()
            if (result != null) _alumniList.value = result
            _loading.value = false
        }
    }

    fun addAlumni(name: String, branch: String, year: String) {
        viewModelScope.launch {
            val newAlumni = repository.addAlumni(AlumniRequest(name, branch, year))
            if (newAlumni != null) {
                _alumniList.value = _alumniList.value + newAlumni
            }
        }
    }

}
