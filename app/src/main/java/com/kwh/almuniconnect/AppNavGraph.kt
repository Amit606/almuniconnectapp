package com.kwh.almuniconnect

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kwh.almuniconnect.almunipost.AlumniFeedScreen
import com.kwh.almuniconnect.branding.MasterTabsScreen
import com.kwh.almuniconnect.evetns.EventDetailsScreen
import com.kwh.almuniconnect.evetns.EventsScreen
import com.kwh.almuniconnect.help.AboutAlumniConnectScreen
import com.kwh.almuniconnect.help.HelpSupportScreen
import com.kwh.almuniconnect.help.WhatsAppChannelsScreen
import com.kwh.almuniconnect.home.HomeScreen
import com.kwh.almuniconnect.intro.IntroScreen
import com.kwh.almuniconnect.jobposting.JobDetailScreen
import com.kwh.almuniconnect.jobposting.JobListingScreen
import com.kwh.almuniconnect.jobposting.JobPostScreen
import com.kwh.almuniconnect.jobposting.sampleJob
import com.kwh.almuniconnect.login.LoginRoute
import com.kwh.almuniconnect.login.PasswordLoginScreen
import com.kwh.almuniconnect.login.RegistrationContainer
import com.kwh.almuniconnect.network.NetworkScreen
import com.kwh.almuniconnect.network.sampleAlumniProfiles
import com.kwh.almuniconnect.otpscreen.OtpVerificationScreen
import com.kwh.almuniconnect.profile.AlumniProfileScreen
import com.kwh.almuniconnect.profile.ProfileScreen
import com.kwh.almuniconnect.settings.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH) {


    NavHost(navController = navController, startDestination = startDestination) {

        // 🟣 Splash Screen
        composable(Routes.SPLASH) {
             SplashScreen(navController)

        }
        composable(Routes.NETWORK) {
            NetworkScreen(
                navController = navController,
                onOpenProfile = { alumni ->
                    navController.navigate(
                        Routes.profileRoute(alumni.id)
                    )
                }
            )
        }
        composable(Routes.USER_PROFILE)
        {
            ProfileScreen(navController)
        }

        composable(
            route = Routes.PROFILE,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            val alumni = sampleAlumniProfiles().first { it.id == id }

            AlumniProfileScreen(
                alumni = alumni,
                navController

            )
        }


        composable(Routes.SPLASH_HOME)
        {
            SplashScreen(navController)
        }

        composable(Routes.LOGIN)
        {
            LoginRoute(navController)

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
                navController,
                onBack = {
                    navController.popBackStack()
                },
                onLogin = { email, password ->
                    // ✅ Handle login
                    // Example:
                    navController.navigate(Routes.HOME)
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
                onFinish = {
                    navController.navigate(Routes.LOGIN) {
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
            EventsScreen(navController)
        }
        composable(Routes.WHATSUP_CHANNEL) {
            WhatsAppChannelsScreen(navController)
        }


        // 🟩 Login
//
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
                onOpenProfile = { navController.navigate(Routes.USER_PROFILE) },
                onOpenMessages = { navController.navigate(Routes.JOB_DETAILS) },
              //  onOpenEventDetails = { event -> navController.navigate(Routes.eventRoute(event.id)) },
                onOpenJobDetails = { job -> navController.navigate(Routes.jobRoute(job.id)) },
                onCreatePost = { navController.navigate(Routes.CREATE_POST) }
            )
        }

        // 🔵 Optional placeholders
        composable(Routes.MESSAGES) { /* MessagesScreen(navController) */ }
        composable(Routes.CREATE_POST) { /* CreatePostScreen(navController) */ }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                navController = navController
            )
        }
        composable(Routes.HELP_SUPPORTS) { HelpSupportScreen(navController) }
        composable(Routes.ABOUT_US) { AboutAlumniConnectScreen(navController) }

//

        composable(
            route = "${Routes.EVENT_DETAILS}/{title}/{location}/{date}/{price}"
        ) { backStack ->
            EventDetailsScreen(
                navController,
                title = backStack.arguments?.getString("title") ?: "",
                location = backStack.arguments?.getString("location") ?: "",
                date = backStack.arguments?.getString("date") ?: "",
                price = backStack.arguments?.getString("price") ?: ""
            )
        }

        composable(Routes.JOB_DETAILS){
            JobListingScreen(navController)
        }
        composable(Routes.JOB_DETAILS_Full){
            JobDetailScreen(sampleJob,navController)
        }
        composable(Routes.JOB_POST){
            JobPostScreen(navController)
        }
        composable(Routes.ALMUNI_POST){
            AlumniFeedScreen(navController)
        }

    }
}
