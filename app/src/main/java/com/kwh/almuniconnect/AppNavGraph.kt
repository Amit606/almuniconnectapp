package com.kwh.almuniconnect

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.kwh.almuniconnect.internet.NoInternetDialog
import com.kwh.almuniconnect.intro.ConnectivityObserver
import com.kwh.almuniconnect.intro.IntroPage
import com.kwh.almuniconnect.intro.IntroScreen
import com.kwh.almuniconnect.intro.NetworkUtils
import com.kwh.almuniconnect.jobposting.JobDetailScreen
import com.kwh.almuniconnect.jobposting.JobListingScreen
import com.kwh.almuniconnect.jobposting.JobPostScreen
import com.kwh.almuniconnect.jobposting.sampleJob
import com.kwh.almuniconnect.login.AuthViewModel
import com.kwh.almuniconnect.login.LoginRoute
import com.kwh.almuniconnect.login.PasswordLoginScreen
import com.kwh.almuniconnect.login.RegistrationContainer
import com.kwh.almuniconnect.network.NetworkScreen
import com.kwh.almuniconnect.network.sampleAlumniProfiles
import com.kwh.almuniconnect.otpscreen.OtpVerificationScreen
import com.kwh.almuniconnect.profile.AlumniProfileScreen
import com.kwh.almuniconnect.profile.ProfileScreen
import com.kwh.almuniconnect.settings.SettingsScreen
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kwh.almuniconnect.almunipost.AlumniStoriesScreen
import com.kwh.almuniconnect.almunipost.AlumniStoryDetailScreen
import com.kwh.almuniconnect.almunipost.dummyAlumniStories
import com.kwh.almuniconnect.jobposting.dummyJobPosts
import com.kwh.almuniconnect.news.NewsListingScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH) {



    val context = LocalContext.current
    val connectivityObserver = remember { ConnectivityObserver(context) }

    val isConnected by connectivityObserver.networkStatus
        .collectAsState(initial = NetworkUtils.isInternetAvailable(context))
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    // 🔁 Navigation side-effect
    LaunchedEffect(isConnected, currentRoute) {

        // 🚫 Do nothing during Splash
        if (currentRoute == Routes.SPLASH) return@LaunchedEffect

        if (!isConnected && currentRoute != Routes.Internet_Splash) {
            navController.navigate(Routes.Internet_Splash) {
                launchSingleTop = true
            }
        }

        if (isConnected && currentRoute == Routes.Internet_Splash) {
            navController.popBackStack()
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {



        // 🟣 Splash Screen
        composable(Routes.SPLASH) {
             SplashScreen(navController)


        }
        composable(Routes.NEWS) {
            NewsListingScreen(navController)

        }
        composable(Routes.Internet_Splash) {
            NoInternetDialog(
                onConnectNow = {  },
                onCancel = { /* Optional */ }
            )

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

            val alumni = sampleAlumniProfiles()
                .firstOrNull { it.id == id }
                ?: return@composable   // 👈 prevents crash

            AlumniProfileScreen(
                alumni = alumni,
                navController = navController
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



        val pages = listOf(
            IntroPage(
                "Connect with Alumni",
                "Stay connected with batchmates, seniors, and the global HBTU alumni network.",
                R.drawable.first
            ),
            IntroPage(
                "Explore Opportunities",
                "Access jobs, referrals, events, and mentorship opportunities from alumni.",
                R.drawable.second
            ),
            IntroPage(
                "Give Back to Harcourtians",
                "Contribute to students, campus initiatives, and the future of HBTU.",
                R.drawable.third
            )

        )

        // 🟢 Intro Screen
        composable(Routes.INTRO) {
            IntroScreen(
                pages= pages,
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
              //  onOpenJobDetails = { job -> navController.navigate(Routes.jobRoute(job.id)) },
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

//        composable(
//            route = "${Routes.EVENT_DETAILS}/{title}/{location}/{date}/{price}"
//        ) { backStack ->
//            EventDetailsScreen(
//                navController,
//                title = backStack.arguments?.getString("title") ?: "",
//                location = backStack.arguments?.getString("location") ?: "",
//                date = backStack.arguments?.getString("date") ?: "",
//                price = backStack.arguments?.getString("price") ?: ""
//            )
//        }
        composable(
            route = "${Routes.EVENT_DETAILS}?title={title}&location={location}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("location") { type = NavType.StringType }
            )
        ) { entry ->
            val title = entry.arguments?.getString("title").orEmpty()
            val location = entry.arguments?.getString("location").orEmpty()

            EventDetailsScreen(navController,title, location,"","")
        }

        composable(Routes.JOB_DETAILS){
            JobListingScreen(navController)
        }

        composable(
            route = "job_details/{jobId}",
            arguments = listOf(
                navArgument("jobId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val jobId = backStackEntry.arguments?.getString("jobId")

            val job = dummyJobPosts.firstOrNull {
                it.jobId == jobId
            }

            job?.let {
                JobDetailScreen(
                    navController = navController,
                    job = it
                )
            }
        }



        composable(Routes.JOB_POST){
            JobPostScreen(navController)
        }
        composable(Routes.ALMUNI_POST){
            AlumniStoriesScreen(navController)
        }
        composable("story_detail/{name}") { backStackEntry ->

            val name = backStackEntry.arguments?.getString("name")

            val story = dummyAlumniStories.first {
                it.name == name
            }

            AlumniStoryDetailScreen(
                navController =navController,
                story = story,
            )
        }

    }
}
