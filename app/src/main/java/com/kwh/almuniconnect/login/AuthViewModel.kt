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

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private fun setLoading(value: Boolean) {
        _loading.value = value
    }
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
                    setLoading(false)

                    onSuccess()
                } else {
                    setLoading(false)

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
         val userPrefs =
            UserPreferences(getApplication())

        setLoading(true)

        viewModelScope.launch {
            repository.checkEmailAndGetUser(email)
                .onSuccess { user ->
                    if (user != null) {
                        setLoading(false)
                        // ✅ NOW THIS WORKS
                        UserSession.saveLogin(getApplication())
                        userPrefs.saveProfile(
                            profile = com.kwh.almuniconnect.storage.UserLocalModel(
                                userId = user.userId,
                                name =user.name.toString(),
                                email = user.email.toString(),
                                photo = user.photoUrl?:"",
                                mobile = user.mobileNo?:"",
                                passoutYear= user.passoutYear?.toInt() ?: 0,
                                branch = user.courseName?:"",
                                year = user.companyName?:"",
                                job = user.companyName?:"",
                                totalExp = user.totalExperience?.toInt() ?: 0,
                                cityName =user.cityName?:"",
                                birthday = user.dateOfBirth?:"",
                                linkedin = user.linkedinUrl?:""
                            )
                        )
//
                        onGoHome()
                    } else {
                        setLoading(false)
                        // ✅ NOW THIS WORKS
                        UserSession.saveLogin(getApplication())
                        userPrefs.saveProfile(
                            profile = com.kwh.almuniconnect.storage.UserLocalModel(
                                name = firebaseUser.displayName.toString(),
                                email = firebaseUser.email.toString(),
                                photo = firebaseUser.photoUrl.toString(),
                                mobile = "",
                                branch = "",
                                year = "",
                                job = "",
                                passoutYear=0,
                                location = "",
                                birthday = "",
                                linkedin = ""
                            )
                        )

                        onGoProfileUpdate()

                    }
                }
                .onFailure {
                    onError(it.message ?: "Login failed")
                }
        }
    }
}


