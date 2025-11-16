// AuthViewModel.kt
package com.kwh.almuniconnect.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.api.SignupRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private fun splitName(fullName: String): Pair<String, String> {
        val parts = fullName.trim().split("\\s+".toRegex())
        return if (parts.size <= 1) {
            (parts.firstOrNull() ?: "") to ""
        } else {
            parts.first() to parts.drop(1).joinToString(" ")
        }
    }

    private fun parseJob(job: String): Pair<String, String> {
        val parts = job.split("/").map { it.trim() }.filter { it.isNotEmpty() }
        return when (parts.size) {
            0 -> "" to ""
            1 -> parts[0] to ""
            else -> parts[0] to parts.drop(1).joinToString(" / ")
        }
    }

    fun register(regData: RegistrationData) {
        viewModelScope.launch {
            _loading.value = true
            _message.value = null

            val (firstName, lastName) = splitName(regData.name)
            val (company, title) = parseJob(regData.jobDetails)

            val request = SignupRequest(
                firstName = firstName.ifEmpty { regData.name },
                lastName = lastName,
                mobileNo = regData.mobile,
                email = regData.email,
                dateOfBirth = regData.dob,        // already yyyy-MM-dd from DatePickerField
                dateOfMarriage = regData.anniversary,
                courseId = 1,
                PassoutYear = regData.passingYear.toIntOrNull() ?: 0,
                companyName = company,
                title = title,
                countryId = 81,
                loggedFrom = "Self",
                deviceToken = ""
            )

            val result = repo.signup(request)
            _loading.value = false

            result.fold(onSuccess = { apiResp ->
                _message.value = apiResp.message ?: "Registration ${if (apiResp.success) "successful" else "failed"}"
            }, onFailure = { err ->
                _message.value = "Error: ${err.message}"
            })
        }
    }
}
