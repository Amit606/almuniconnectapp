package com.kwh.almuniconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kwh.almuniconnect.ui.theme.AlmuniconnectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlmuniconnectTheme {
                AppNavGraph(startDestination = Routes.SPLASH)

//                val navController = rememberNavController()
//
//                NavHost(navController = navController, startDestination = "splash") {
//                    composable("splash") { SplashScreen(navController) }
//                    composable("intro") { IntroScreen(navController) }
//                    composable("login") {
//                        LoginScreen(
//                            onLogin = { email, password ->
//                                // Handle login logic here (via ViewModel)
//                                // Example: authViewModel.login(email, password)
//                                // On success:
//                                navController.navigate("home") {
//                                    popUpTo("login") { inclusive = true }
//                                }
//                            },
//                            onGoogleSignIn = {
//                                // Launch Google sign-in
//                                // e.g. googleSignInLauncher.launch(googleSignInClient.signInIntent)
//                            },
//                            onForgotPassword = {
//                                // Navigate to forgot password screen if exists
//                                navController.navigate("forgot_password")
//                            },
//                            onCreateAccount = {
//                                // Navigate to sign-up screen
//                                navController.navigate("register")
//                            }
//                        )
//                    }
//                    composable ("started",){
//                        GetStartedCard(
//                            onJoinNow = {
//                                navController.navigate("login")   // 👈 Navigate to LoginScreen
//                            }
//                        )
//                    }
//                    composable("register") {
//                        RegistrationScreen(
//                            onRegister = { registrationData ->
//                                // Handle registration logic here (ViewModel or backend call)
//                                println("User Registered: $registrationData")
//
//                                // After registration success, navigate to home
//                                navController.navigate("home") {
//                                    popUpTo("register") { inclusive = true }
//                                }
//                            }
//                        )
//                    }

                    // Optional: other screens
                  //  composable("register") { RegisterScreen(navController) }
                  //  composable("forgot_password") { ForgotPasswordScreen(navController) }
                  //  composable("home") { HomeScreen(navController) }
                  //  composable("login",{LoginScreen(onLogin = { _, _ -> }, onGoogleSignIn = { }})
//                    composable("language") { LanguageSelectionScreen(navController) }
//                    composable("home") { HomeScreen(navController) }
//                    composable("cleaner") { CleanerScreen(navController) }
//                    composable("shareFiles") { ShareFilesScreen(navController) }
//                    composable("appLock") { AppLockScreen(navController) }
//                    composable("calendar") { CalendarScreen(navController ) }
//                    composable("scanner") { ScannerScreen(navController) }
//                    composable("statusSaver") { StatusSaverComposeScreen(navController) }


//                    composable("speedTest") { InternetSpeedTestScreen(navController) }
//                    composable("storageInfo") { StorageInfoScreen(navController) }
//
//
//// Compose:
//
//                    composable("recycleBin") { RecycleBinScreen(navController = navController, viewModel = viewModel) }
//                    composable("batteryInfo") { BatteryInfoScreen(navController) }
//                    composable("more") { PromoteNavGraph(navController.toString(),"promote_list") }
//                    composable("subscription") { SubscriptionScreen(navController) }
//                    composable  ("settings"){SettingsScreen(navController)}
//                    composable  ("about"){ AboutUsScreen(navController) }

                }
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlmuniconnectTheme {
        Greeting("Android")
    }
}