package com.kwh.almuniconnect.login

import android.app.Application
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember


@Composable
fun LoginRoute(
    navController: NavController,
) {
    val context = LocalContext.current
    val apiService = remember {
        NetworkClient.createService(ApiService::class.java)
    }
    val repository = remember { AuthRepository(apiService) }


    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            application = context.applicationContext as Application,
            repository = repository
        )
    )
    val isLoading by viewModel.loading.collectAsState()

    TrackScreen("login_screen")

    // Google Sign-In config
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken ?: return@rememberLauncherForActivityResult

                // ✅ STEP 1: Firebase Authentication
                viewModel.firebaseAuthWithGoogle(
                    idToken = idToken,

                    onSuccess = {
                        val firebaseUser =
                            FirebaseAuth.getInstance().currentUser
                                ?: return@firebaseAuthWithGoogle

                        // ✅ STEP 2: Backend email check
                        viewModel.onGoogleLoginSuccess(
                            firebaseUser = firebaseUser,

                            onGoHome = {
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            },

                            onGoProfileUpdate = {
                                navController.navigate(Routes.USER_PROFILE) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            },

                            onError = {
                                Log.e("GoogleLogin", it)
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

    Box(modifier = Modifier.fillMaxSize()) {

        AlumniLoginScreen(
            onGoogleLogin = {
                launcher.launch(googleSignInClient.signInIntent)
            }
        )

        if (isLoading) {
            LoadingOverlay()
        }
    }
}
@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeWidth = 4.dp
        )
    }
}
