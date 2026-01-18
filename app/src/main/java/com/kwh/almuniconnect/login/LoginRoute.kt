package com.kwh.almuniconnect.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.AnalyticsEvent
import com.kwh.almuniconnect.analytics.AnalyticsManager
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.storage.UserPreferences
import com.kwh.almuniconnect.storage.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    navController: NavController,
    viewModel: AuthViewModel = viewModel() // ⬅️ THIS LINE
) {
    val context = LocalContext.current

    // 🔹 Create UserPreferences ONCE

    TrackScreen("login_screen")

    // 🔹 Inject ViewModel with factory

    // 🔹 Google Sign-In config
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    // 🔹 Activity result launcher
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken ?: return@rememberLauncherForActivityResult

                viewModel.firebaseAuthWithGoogle(
                    idToken = idToken,
                    onSuccess = {
                        val firebaseUser =
                            FirebaseAuth.getInstance().currentUser ?: return@firebaseAuthWithGoogle

                        // 🔥 HAND OVER TO VIEWMODEL
                        viewModel.onGoogleLoginSuccess(
                            firebaseUser = firebaseUser,
                            onNavigate = {
                                AnalyticsManager.logEvent(
                                    AnalyticsEvent.ScreenView("user_profile_screen")
                                )
                                navController.navigate(Routes.USER_PROFILE) {

                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            }
                        )
                    },
                    onError = {
                        Log.e("GoogleLogin", it)
                    }
                )
            } catch (e: ApiException) {
                Log.e("GoogleLogin", e.localizedMessage ?: "Google Sign-In failed")
            }
        }

    // 🔹 UI
    AlumniLoginScreen(

        onGoogleLogin = {
            launcher.launch(googleSignInClient.signInIntent)
        }
    )
}
