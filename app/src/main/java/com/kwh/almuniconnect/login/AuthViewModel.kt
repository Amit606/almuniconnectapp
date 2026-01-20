// AuthViewModel.kt
package com.kwh.almuniconnect.login

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kwh.almuniconnect.storage.UserPreferences
import com.kwh.almuniconnect.storage.UserSession
import kotlinx.coroutines.delay

class AuthViewModel(
    application: Application,
    private val repository: AuthRepository
) : AndroidViewModel(application) {

    fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.localizedMessage ?: "Firebase auth failed")
                }
            }
    }

    fun onGoogleLoginSuccess(
        firebaseUser: FirebaseUser,
        onGoHome: () -> Unit,
        onGoProfileUpdate: () -> Unit,
        onError: (String) -> Unit
    ) {
        val email = firebaseUser.email ?: return onError("Email not found")

        viewModelScope.launch {
            repository.checkEmailAndGetUser(email)
                .onSuccess { user ->
                    if (user != null) {
                        // ✅ NOW THIS WORKS
                        UserSession.saveLogin(getApplication())
                        onGoHome()
                    } else {
                        onGoProfileUpdate()
                    }
                }
                .onFailure {
                    onError(it.message ?: "Login failed")
                }
        }
    }
}


