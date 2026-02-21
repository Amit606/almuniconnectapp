package com.kwh.almuniconnect.verification

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PendingVerificationViewModel : ViewModel() {

    private val repository = AlumniRepository()

    var uiState by mutableStateOf<UiState>(UiState.Loading)
        private set

    fun loadPending(alumniId: String) {
        viewModelScope.launch {

            uiState = UiState.Loading

            val result = repository.getPending(alumniId)

            uiState = result.fold(
                onSuccess = { list ->
                    if (list.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(list)
                    }
                },
                onFailure = {
                    UiState.Error(it.message ?: "Something went wrong")
                }
            )
        }
    }
    fun approveAlumni(context:Context,alumniId: String) {
        viewModelScope.launch {

            val result = repository.verifyAlumni(context,
                alumniId = alumniId,
                isVerified = true
            )

            result.fold(
                onSuccess = {

                    val currentList =
                        (uiState as? UiState.Success)?.data ?: return@launch

                    val updatedList =
                        currentList.filter { it.alumniId != alumniId }

                    uiState = if (updatedList.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(updatedList)
                    }
                },
                onFailure = {
                    uiState = UiState.Error("Verification failed")
                }
            )
        }
    }
    fun denyAlumni(context:Context,alumniId: String) {
        viewModelScope.launch {

            val result = repository.verifyAlumni(context,
                alumniId = alumniId,
                isVerified = false
            )

            result.fold(
                onSuccess = {

                    val currentList =
                        (uiState as? UiState.Success)?.data ?: return@launch

                    val updatedList =
                        currentList.filter { it.alumniId != alumniId }

                    uiState = if (updatedList.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(updatedList)
                    }
                },
                onFailure = {
                    uiState = UiState.Error("Verification failed")
                }
            )
        }
    }
    fun isAlumniVerified(
        alumniId: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            val isVerified = repository.isAlumniVerified(alumniId)

            onResult(isVerified)
        }
    }

    fun setEmptyState() {
        uiState = UiState.Empty
    }

}

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<AlumniData>) : UiState()
    object Empty : UiState()
    data class Error(val message: String) : UiState()
}
