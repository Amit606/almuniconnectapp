package com.kwh.almuniconnect.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.api.SignupRequest
import com.kwh.almuniconnect.login.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val userId: String) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> = _state

    fun submitProfile(
        request: SignupRequest
    ) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
             Log.e("ProfileViewModel", "Submitting profile with request: $request")
            repository.signup(request)
                .onSuccess { response ->
                    if (response.success && response.data != null) {
                        _state.value = ProfileState.Success(response.data.userId)
                    } else {
                        _state.value = ProfileState.Error(response.message)
                    }
                }
                .onFailure { throwable ->
                    _state.value = ProfileState.Error(
                        throwable.message ?: "Something went wrong"
                    )
                }
        }
    }
}
