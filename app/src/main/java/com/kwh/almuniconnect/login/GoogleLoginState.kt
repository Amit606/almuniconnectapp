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


