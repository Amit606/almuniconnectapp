package com.kwh.almuniconnect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.messaging.FirebaseMessaging
import com.kwh.almuniconnect.ui.theme.AlumniConnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlumniConnectTheme {
                AppNavGraph(startDestination = Routes.USER_PROFILE)
                LaunchedEffect(Unit) {
                    FirebaseMessaging.getInstance().token.addOnSuccessListener {
                        Log.d("FCM_TOKEN", it)
                    }
                }
            }
        }
    }
}
