package com.kwh.almuniconnect.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.storage.UserPreferences
import com.kwh.almuniconnect.storage.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

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
                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        val userPrefs = UserPreferences(context)

                        firebaseUser?.let { user ->
                            // ✅ SAVE LOCALLY
                            CoroutineScope(Dispatchers.IO).launch {
                                userPrefs.saveUser(
                                    uid = user.uid,
                                    name = user.displayName,
                                    email = user.email,
                                    photo = user.photoUrl?.toString()
                                )
                            }


                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            UserSession.saveLogin(context)
                        }

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true
                            }
                        }

                    },
                    onError = {
                        Log.e("GoogleLogin", it)
                    }
                )
            } catch (e: ApiException) {
                Log.e("GoogleLogin", e.localizedMessage ?: "Google Sign-In failed")
            }
        }
    //            AlumniLoginScreen(
//                onRequestOtp = { email ->
//                    // 🔹 Handle Request OTP
//                    // Example:
//                     navController.navigate("otp/$email")
//                },
//                onLoginWithPassword = {
//                    // 🔹 Navigate to Password Login
//                     navController.navigate(Routes.PASSWORD_LOGIN)
//                },
//                onGoogleLogin = {
//                    // 🔹 Google Sign-In
//                }
//            )

    // 🔹 UI
    AlumniLoginScreen(
        onRequestOtp = {email->
          navController.navigate("otp/$email")

            // optional OTP flow
        },
        onLoginWithPassword = {
        navController.navigate(Routes.PASSWORD_LOGIN)
        },
        onGoogleLogin = {
            launcher.launch(googleSignInClient.signInIntent)
        }
    )
}
