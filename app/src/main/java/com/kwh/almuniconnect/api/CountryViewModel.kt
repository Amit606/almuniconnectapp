package com.kwh.almuniconnect.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryViewModel : ViewModel() {

    private val api = NetworkClient.createService(ApiService::class.java)
    private val repository = NetworkRepository(api)

    private val _countries = MutableStateFlow<List<MasterItem>>(emptyList())

    val countries: StateFlow<List<MasterItem>> = _countries

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun getCountries() {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.fetchCountries()
            if (result != null) {
                _countries.value = result
            }
            _loading.value = false
        }
    }
}
