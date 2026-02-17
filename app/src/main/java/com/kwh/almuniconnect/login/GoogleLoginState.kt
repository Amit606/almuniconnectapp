package com.kwh.almuniconnect.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class GoogleLoginState {
    object Loading : GoogleLoginState()
    data class GoHome(val user: ExistingUserDto) : GoogleLoginState()
    object GoProfileUpdate : GoogleLoginState()
    data class Error(val message: String) : GoogleLoginState()
}

class GoogleLoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<GoogleLoginState?>(null)
    val state: StateFlow<GoogleLoginState?> = _state

    fun handleGoogleLogin(email: String) {
        viewModelScope.launch {
            _state.value = GoogleLoginState.Loading

            repository.checkEmailAndGetUser(email)
                .onSuccess { user ->
                    _state.value =
                        if (user != null) {

                            GoogleLoginState.GoHome(user)
                        }
                        else {
                            GoogleLoginState.GoProfileUpdate
                        }
                }
                .onFailure {
                    _state.value = GoogleLoginState.Error(
                        it.message ?: "Login failed"
                    )
                }
        }
    }
}
