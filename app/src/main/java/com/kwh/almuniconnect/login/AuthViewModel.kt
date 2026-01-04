// AuthViewModel.kt
package com.kwh.almuniconnect.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.api.SignupRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.GoogleAuthProvider

//class AuthViewModel(private val repo: AuthRepository) : ViewModel() {
//
//    private val _loading = MutableStateFlow(false)
//    val loading: StateFlow<Boolean> = _loading
//
//    private val _message = MutableStateFlow<String?>(null)
//    val message: StateFlow<String?> = _message
//
//    private fun splitName(fullName: String): Pair<String, String> {
//        val parts = fullName.trim().split("\\s+".toRegex())
//        return if (parts.size <= 1) {
//            (parts.firstOrNull() ?: "") to ""
//        } else {
//            parts.first() to parts.drop(1).joinToString(" ")
//        }
//    }
//
//    private fun parseJob(job: String): Pair<String, String> {
//        val parts = job.split("/").map { it.trim() }.filter { it.isNotEmpty() }
//        return when (parts.size) {
//            0 -> "" to ""
//            1 -> parts[0] to ""
//            else -> parts[0] to parts.drop(1).joinToString(" / ")
//        }
//    }
//
//    fun register(regData: RegistrationData) {
//        viewModelScope.launch {
//            _loading.value = true
//            _message.value = null
//
//            val (firstName, lastName) = splitName(regData.firstName)
//            val (company, title) = parseJob(regData.jobDetails)
//
//            val request = SignupRequest(
//                firstName = firstName.ifEmpty { regData.firstName },
//                lastName = lastName,
//                mobileNo = regData.mobileNo,
//                email = regData.email,
//                dateOfBirth = regData.dateOfBirth,        // already yyyy-MM-dd from DatePickerField
//                dateOfMarriage = regData.dateOfMarriage,
//                courseId = 1,
//                PassoutYear = regData.PassoutYear.toIntOrNull() ?: 0,
//                companyName = company,
//                title = title,
//                countryId = 81,
//                loggedFrom = "Self",
//                deviceToken = "sxsssx"
//            )
//            Log.e("Registration",request.toString())
//
//            val result = repo.signup(request)
//            _loading.value = false
//
//            result.fold(onSuccess = { apiResp ->
//                _message.value = apiResp.message ?: "Registration ${if (apiResp.success) "successful" else "failed"}"
//            }, onFailure = { err ->
//                _message.value = "Error: ${err.message}"
//            })
//        }
//    }
//}
class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var verificationId: String? = null

    fun sendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: () -> Unit,
        onError: (String) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // 🔥 AUTO-FILL OTP
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onError(e.localizedMessage ?: "OTP failed")
                }

                override fun onCodeSent(
                    id: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    verificationId = id
                    onCodeSent()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(
            verificationId ?: return,
            otp
        )

        signInWithCredential(
            credential,
            onSuccess,
            onError
        )
    }

    private fun signInWithCredential(
        credential: PhoneAuthCredential,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.localizedMessage ?: "Invalid OTP")
            }
    }
    fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.localizedMessage ?: "Google Sign-In failed")
            }
    }
}