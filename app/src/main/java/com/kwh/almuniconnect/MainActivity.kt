package com.kwh.almuniconnect

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.drivemetadata.DriveMetaData
import com.drivemetadata.callbacks.DriveMetaDataCallbacks
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.kwh.almuniconnect.storage.FcmPrefs
import com.kwh.almuniconnect.ui.theme.LinkedTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class MainActivity : ComponentActivity(),DriveMetaDataCallbacks {
    private lateinit var updateHelper: InAppUpdateHelper
    private var notificationIntent by mutableStateOf<Intent?>(null)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        tokenGeneration(this)

        updateHelper = InAppUpdateHelper(this)
        updateHelper.checkUpdate(this)
        // get cold-start notification
        notificationIntent = intent
        DriveMetaData.with(this).setDriveMetaDataCallbacks(this::onResponse);




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

    override fun onResponse(p0: Boolean, p1: String?, p2: String?) {
        Log.d(
            "MainActivity",
            "status " + p0 + " message " + p1 + "requestAcknowledgementID =====    > " + p2
        )

    }
}
private fun tokenGeneration(context: Context) {

    val appContext = context.applicationContext

    FirebaseMessaging.getInstance().token
        .addOnSuccessListener { newToken ->

            if (newToken.isNullOrBlank()) return@addOnSuccessListener

            // ✅ Lifecycle-safe background work
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {

                try {
                    val savedToken = FcmPrefs.getToken(appContext)
                    uploadTokenToServer(newToken,context)

                    if (savedToken == newToken) {
                        Log.d("FCM", "Token unchanged → $newToken")
                        return@launch
                    }

                    // 🔥 Optional backend update
                     uploadTokenToServer(newToken,context)

                    FcmPrefs.saveToken(appContext, newToken)

                    Log.d("FCM", "Token updated → $newToken")

                } catch (e: Exception) {
                    Log.w("FCM", "Token save failed", e)
                }
            }
        }
        .addOnFailureListener { e ->
            // ✅ NO CRASH
            Log.w("FCM", "FCM token fetch failed (safe to ignore)", e)
        }
}

private fun uploadTokenToServer(token: String,context: Context) {
    // Create the JSON structure to send the new token to the API
    val deviceToken = JSONObject()
    val userIdentifier = JSONObject()
    val deviceTokenObject = JSONObject()
    try {
        deviceToken.put("device_token", token)
        userIdentifier.put("mobile", "7905717240") // Replace with actual user data
        userIdentifier.put("customer_id", "9789") // Replace with actual user data
        deviceTokenObject.put("deviceNotificationToken", deviceToken)
        deviceTokenObject.put("userIdentifier", userIdentifier)

        // Send the token update via API call (or any other service you're using)
        DriveMetaData.with(context).sendTags(deviceTokenObject.toString(), "deviceToken")
    } catch (e: JSONException) {
        Log.e("Token", "Uploading token to server: $e")

        e.printStackTrace()
    }




}

private fun handleNotificationIntent(
    intent: Intent?,
    navController: NavHostController
) {
    if (intent == null) return

    val fromNotification = intent.getBooleanExtra("from_notification", false)
    if (!fromNotification) return

    val destination = intent.getStringExtra("destination")

    when (destination) {


        Routes.JOB_DETAILS_Full -> {
            navController.safeNavigate(Routes.JOB_DETAILS_Full)
        }
        Routes.VERIFICATION -> {
            navController.safeNavigate(Routes.VERIFICATION)
        }
        Routes.APPROVAL_PENDING -> {
            navController.safeNavigate(Routes.APPROVAL_PENDING)
        }

        Routes.ALMUNI_POST -> {
            navController.safeNavigate(Routes.ALMUNI_POST)
        }

        Routes.EVENTS -> {
            navController.safeNavigate(Routes.EVENTS)
        }

        Routes.WHATSUP_CHANNEL -> {
            navController.safeNavigate(Routes.WHATSUP_CHANNEL)
        }

        Routes.HOME, null -> {
            navController.safeNavigate(Routes.HOME)
        }

        // 🔥 UNKNOWN ROUTE → HOME
        else -> {
            FirebaseCrashlytics.getInstance().log(
                "Unknown notification route: $destination"
            )
            navController.safeNavigate(Routes.HOME)
        }
    }
}

fun NavHostController.safeNavigate(
    route: String,
    fallbackRoute: String = Routes.HOME
) {
    try {
        navigate(route) {
            popUpTo(fallbackRoute) { inclusive = false }
            launchSingleTop = true
        }
    } catch (e: IllegalArgumentException) {
        // 🔥 Log crash but don't crash app
        FirebaseCrashlytics.getInstance().recordException(e)

        navigate(fallbackRoute) {
            popUpTo(fallbackRoute) { inclusive = true }
            launchSingleTop = true
        }
    }
}

