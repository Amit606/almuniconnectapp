package com.kwh.almuniconnect.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _deleteState = MutableStateFlow<Boolean?>(null)
    val deleteState: StateFlow<Boolean?> = _deleteState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun deleteAccount() {
        viewModelScope.launch {
            _loading.value = true
            val success = repository.deleteCompleteAccount()
            _deleteState.value = success
            _loading.value = false
        }
    }
}


