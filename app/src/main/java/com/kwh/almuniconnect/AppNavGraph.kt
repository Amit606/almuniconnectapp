package com.kwh.almuniconnect

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kwh.almuniconnect.login.LoginScreen
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.kwh.almuniconnect.branding.MasterTabsScreen
import com.kwh.almuniconnect.events.EventScreen
import com.kwh.almuniconnect.home.HomeScreen
import com.kwh.almuniconnect.intro.IntroScreen
import com.kwh.almuniconnect.login.AlumniLoginScreen
import com.kwh.almuniconnect.login.PasswordLoginScreen
import com.kwh.almuniconnect.login.RegistrationContainer
import com.kwh.almuniconnect.network.NetworkScreen
import com.kwh.almuniconnect.otpscreen.OtpVerificationScreen

@Composable
fun AppNavGraph(startDestination: String = Routes.SPLASH) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        // 🟣 Splash Screen
        composable(Routes.SPLASH) {
           // SplashScreen(navController)
           // JobListingScreen()
          //  EventScreen(navController)
            AlumniLoginScreen(
                onRequestOtp = { email ->
                    // 🔹 Handle Request OTP
                    // Example:
                     navController.navigate("otp/$email")
                },
                onLoginWithPassword = {
                    // 🔹 Navigate to Password Login
                     navController.navigate(Routes.PASSWORD_LOGIN)
                },
                onGoogleLogin = {
                    // 🔹 Google Sign-In
                }
            )


        }
        // 🔢 OTP Screen
        composable(
            route = "${Routes.OTP}/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email =
                backStackEntry.arguments?.getString("email") ?: ""

            OtpVerificationScreen(
                navController,
                email = email,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PASSWORD_LOGIN) {
            PasswordLoginScreen(
                onBack = {
                    navController.popBackStack()
                },
                onLogin = { email, password ->
                    // ✅ Handle login
                    // Example:
                    // viewModel.loginWithEmail(email, password)
                },
                onForgotPassword = {
                    // Navigate to forgot password
                }
            )
        }
//        composable(Routes.COUTNRYLIST) {
//            CountryListScreen()
//        }

        // 🟢 Intro Screen
        composable(Routes.INTRO) {
            IntroScreen(
                onContinue = {
                    navController.navigate(Routes.STARTED) {
                        popUpTo(Routes.INTRO) { inclusive = true }
                    }
                }
            )
        }

        // 🟦 Get Started
        composable(Routes.STARTED) {
            GetStartedCard(
                onJoinNow = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(Routes.EVENTS) {
            EventScreen(navController)
        }


        // 🟩 Login
        composable(Routes.LOGIN) {
            val context = LocalContext.current
            val prefs = remember { PreferenceHelper(context) }
            LoginScreen(
                onLogin = { email, password ->
                    prefs.setLoginStatus(true)
                    prefs.saveUserData(name = "John Doe", email = email) // use real data
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoogleSignIn = { /* handle Google sign-in */ },
                onForgotPassword = { /* navController.navigate("forgot_password") */ },
                onCreateAccount = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.COUTNRYLIST) {
            MasterTabsScreen(onItemClick = { masterItem ->
                // handle selection: navigate back, open edit screen, etc.
            })
        }

        // 🟨 Registration
        composable(Routes.REGISTER) {
            RegistrationContainer()
//            RegistrationScreen(
//                onRegister = {
//                    navController.navigate(Routes.HOME) {
//                        popUpTo(Routes.REGISTER) { inclusive = true }
//                    }
//                }
//            )
        }

        // 🟧 Home
        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                onOpenProfile = { navController.navigate(Routes.profileRoute("me")) },
                onOpenMessages = { navController.navigate(Routes.MESSAGES) },
                onOpenEventDetails = { event -> navController.navigate(Routes.eventRoute(event.id)) },
                onOpenJobDetails = { job -> navController.navigate(Routes.jobRoute(job.id)) },
                onCreatePost = { navController.navigate(Routes.CREATE_POST) }
            )
        }

        // 🔵 Optional placeholders
        composable(Routes.MESSAGES) { /* MessagesScreen(navController) */ }
        composable(Routes.CREATE_POST) { /* CreatePostScreen(navController) */ }

        composable(
            Routes.PROFILE,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            /* ProfileScreen(userId) */
        }

        composable(
            Routes.EVENT_DETAILS,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            /* EventDetailsScreen(eventId) */
        }
        composable(Routes.NETWORK) {
            NetworkScreen(onOpenProfile = { alumni ->
                navController.navigate(Routes.profileRoute(alumni.id))
            })
        }

        composable(
            Routes.JOB_DETAILS,
            arguments = listOf(navArgument("jobId") { type = NavType.StringType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")
            /* JobDetailsScreen(jobId) */
        }
    }
}
