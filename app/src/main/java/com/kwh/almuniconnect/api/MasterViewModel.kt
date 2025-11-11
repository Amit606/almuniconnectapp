package com.kwh.almuniconnect.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MasterViewModel(
    private val repository: NetworkRepository = NetworkRepository(NetworkClient.createService(ApiService::class.java))
) : ViewModel() {

    private val _countries = MutableStateFlow<List<MasterItem>>(emptyList())
    val countries: StateFlow<List<MasterItem>> = _countries

    private val _branches = MutableStateFlow<List<MasterItem>>(emptyList())
    val branches: StateFlow<List<MasterItem>> = _branches

    private val _batches = MutableStateFlow<List<MasterItem>>(emptyList())
    val batches: StateFlow<List<MasterItem>> = _batches

    private val _roles = MutableStateFlow<List<MasterItem>>(emptyList())
    val roles: StateFlow<List<MasterItem>> = _roles

    private val _courses = MutableStateFlow<List<MasterItem>>(emptyList())
    val courses: StateFlow<List<MasterItem>> = _courses

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    /** public helpers to load each tab */
    fun loadCountries() = viewModelScope.launch { fetchAndSet(_countries) { repository.fetchCountries() } }
    fun loadBranches() = viewModelScope.launch { fetchAndSet(_branches) { repository.fetchBranches() } }
    fun loadBatches()  = viewModelScope.launch { fetchAndSet(_batches)  { repository.fetchBatches() } }
    fun loadRoles()    = viewModelScope.launch { fetchAndSet(_roles)    { repository.fetchRoles() } }
    fun loadCourses()  = viewModelScope.launch { fetchAndSet(_courses)  { repository.fetchCourses() } }

    private suspend fun fetchAndSet(state: MutableStateFlow<List<MasterItem>>, fetcher: suspend () -> List<MasterItem>?) {
        _loading.value = true
        val result = try { fetcher() } catch (e: Exception) { null }
        result?.let { state.value = it } ?: run { /* keep previous or empty */ }
        _loading.value = false
    }
}
