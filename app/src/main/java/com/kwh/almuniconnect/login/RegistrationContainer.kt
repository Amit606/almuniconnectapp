// RegistrationContainer.kt
package com.kwh.almuniconnect.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegistrationContainer() {
    val factory = AuthViewModelFactory()
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val loading by authViewModel.loading.collectAsState()
    val message by authViewModel.message.collectAsState()
    val context = LocalContext.current
    var lastSentData by remember { mutableStateOf<RegistrationData?>(null) }

    LaunchedEffect(message) {
        message?.let {
            Log.e("Registration",message.toString())

            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        RegistrationScreen(onRegister = { regData ->
            authViewModel.register(regData)
        }, modifier = Modifier.fillMaxSize())
        if (loading) {
            lastSentData?.let {
                Log.d("RegisterData", "Sending to API: $it")
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
