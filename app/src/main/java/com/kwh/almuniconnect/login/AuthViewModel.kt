// AuthViewModel.kt
package com.kwh.almuniconnect.login

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kwh.almuniconnect.storage.UserPreferences
import com.kwh.almuniconnect.storage.UserSession
import kotlinx.coroutines.delay



class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPrefs =
        UserPreferences(application.applicationContext)

    private val auth = FirebaseAuth.getInstance()

    fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSuccess()
                else onError(task.exception?.message ?: "Auth failed")
            }
    }

    fun onGoogleLoginSuccess(
        firebaseUser: FirebaseUser,
        onNavigate: () -> Unit
    ) {
        viewModelScope.launch {
            userPrefs.saveUser(
                uid = firebaseUser.uid,
                name = firebaseUser.displayName,
                email = firebaseUser.email,
                photo = firebaseUser.photoUrl?.toString()
            )

          //  UserSession.saveLogin(firebaseUser.uid)

            delay(300)
            onNavigate()
        }
    }
}

