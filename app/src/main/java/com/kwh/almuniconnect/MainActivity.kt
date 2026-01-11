package com.kwh.almuniconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.messaging.FirebaseMessaging
import com.kwh.almuniconnect.ui.theme.AlumniConnectTheme

class MainActivity : ComponentActivity() {
    private lateinit var updateHelper: InAppUpdateHelper
    private var startIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateHelper = InAppUpdateHelper(this)
        updateHelper.checkUpdate(this)
        setContent {
            AlumniConnectTheme {
                AppNavGraph(startDestination = Routes.SPLASH)
                LaunchedEffect(Unit) {
                    FirebaseMessaging.getInstance().token.addOnSuccessListener {
                        Log.d("FCM_TOKEN", it)
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        updateHelper.onResume(this)
    }
}
