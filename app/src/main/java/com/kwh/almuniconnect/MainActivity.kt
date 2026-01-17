package com.kwh.almuniconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.kwh.almuniconnect.storage.FcmPrefs
import com.kwh.almuniconnect.ui.theme.LinkedTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var updateHelper: InAppUpdateHelper
    private var notificationIntent by mutableStateOf<Intent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        tokenGeneration(this)

        updateHelper = InAppUpdateHelper(this)
        updateHelper.checkUpdate(this)



        // get cold-start notification
        notificationIntent = intent







        setContent {
            LinkedTheme {

                val navController = rememberNavController()



                // Handle FCM click safely

                LaunchedEffect(notificationIntent) {
                    notificationIntent?.let {
                        handleNotificationIntent(it, navController)
                        notificationIntent = null   // prevent repeat
                    }
                }
                AppNavGraph(
                    navController = navController,
                    startDestination = Routes.SPLASH
                )


            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateHelper.onResume(this)
    }
    // When app is already running and notification is clicked
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        notificationIntent = intent
    }
}
private fun tokenGeneration( activity: MainActivity) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->

        if (!task.isSuccessful) {
            Log.w("FCM", "Token fetch failed", task.exception)
            return@addOnCompleteListener
        }

        val newToken = task.result ?: return@addOnCompleteListener

        CoroutineScope(Dispatchers.IO).launch {

            val savedToken = FcmPrefs.getToken(activity)

            if (savedToken == newToken) {
                Log.d("FCM", "Token unchanged →"+newToken)
                return@launch
            }

            // 🔥 Token changed → update backend
          //  uploadTokenToServer(newToken)

            // Save locally
            FcmPrefs.saveToken(activity, newToken)

            Log.d("FCM", "Token updated: $newToken")
        }
    }
}
private fun handleNotificationIntent(
    intent: Intent?,
    navController: NavHostController
) {
    if (intent == null) return

    val fromNotification = intent.getBooleanExtra("from_notification", false)
    if (!fromNotification) return

    when (intent.getStringExtra("destination")) {
        Routes.JOB_DETAILS_Full   -> {
            navController.navigate(Routes.JOB_DETAILS_Full) {
                popUpTo("home") { inclusive = false }
            }
        }

        Routes.ALMUNI_POST  -> {
            navController.navigate(Routes.ALMUNI_POST) {
                popUpTo("home") { inclusive = false }
            }
        }
        Routes.EVENTS -> {
            navController.navigate(Routes.EVENTS) {
                popUpTo("home") { inclusive = false }
            }
        }
        Routes.WHATSUP_CHANNEL -> {
            navController.navigate(Routes.WHATSUP_CHANNEL) {
                popUpTo("home") { inclusive = false }
            }
        }


        // Add more destinations in future if needed
        "home" -> navController.navigate(Routes.HOME)
    }

}
