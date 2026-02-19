package com.kwh.almuniconnect

import AlumniViewModel
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.kwh.almuniconnect.login.LoginRoute
import com.kwh.almuniconnect.network.NetworkScreen
import com.kwh.almuniconnect.profile.ProfileScreen
import com.kwh.almuniconnect.settings.SettingsScreen
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kwh.almuniconnect.almunipost.AlumniStoriesScreen
import com.kwh.almuniconnect.almunipost.AlumniStoryDetailScreen
import com.kwh.almuniconnect.almunipost.alumniFeed
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.branding.ProductDetailsScreen
import com.kwh.almuniconnect.branding.ProductServiceDummyScreen
import com.kwh.almuniconnect.emergency.DonateAmountScreen
import com.kwh.almuniconnect.emergency.DonationSuccessScreen
import com.kwh.almuniconnect.emergency.EmergencyDetailScreen
import com.kwh.almuniconnect.emergency.EmergencyFeedScreen
import com.kwh.almuniconnect.emergency.EmergencyRequestForm
import com.kwh.almuniconnect.emergency.demoEmergencyList
import com.kwh.almuniconnect.jobposting.dummyJobPosts
import com.kwh.almuniconnect.network.AlumniRepository
import com.kwh.almuniconnect.network.AlumniViewModelFactory
import com.kwh.almuniconnect.news.NewsListingScreen
import com.kwh.almuniconnect.profile.AlumniProfileRoute
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.subscription.PremiumScreen
import com.kwh.almuniconnect.verification.AccountVerificationScreen

@RequiresApi(Build.VERSION_CODES.O)
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

           // EmergencyFeedScreen(navController)

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


        composable(Routes.USER_PROFILE)
        {
            ProfileScreen(navController)
        }
        composable(Routes.NETWORK) {
            val apiService = remember {
                NetworkClient.createService(ApiService::class.java)
            }
            val repository = remember { AlumniRepository(apiService) }

            val alumniViewModel: AlumniViewModel = viewModel(
                factory = AlumniViewModelFactory(repository)
            )

            NetworkScreen(
                navController = navController,
                onOpenProfile = { alumni ->
                    navController.navigate(
                        Routes.profileRoute(alumni.alumniId)
                    )
                }
            )
        }

        composable(
            route = Routes.PROFILE_ROUTE,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { entry ->

            val id = entry.arguments?.getString("id")
                ?: return@composable

            // 🔥 SAME VIEWMODEL INSTANCE
            val alumniViewModel: AlumniViewModel = viewModel(
                factory = AlumniViewModelFactory(
                    AlumniRepository(
                        NetworkClient.createService(ApiService::class.java)
                    )
                )
            )

            AlumniProfileRoute(
                alumniId = id,
                navController = navController,
                viewModel = alumniViewModel
            )
        }

//        composable(Routes.NETWORK) {
//            NetworkScreen(
//                navController = navController,
//                onOpenProfile = { alumni ->
//                    navController.navigate(
//                        Routes.profileRoute(alumni.alumniId)
//                    )
//                }
//            )
//        }
//        composable(
//            route = Routes.PROFILE_ROUTE,
//            arguments = listOf(
//                navArgument("id") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//
//            val id = backStackEntry.arguments?.getString("id")
//                ?: return@composable
//
//            AlumniProfileScreen(
//                alumniId = id,
//                navController = navController
//            )
//        }
        val user = UserLocalModel(
            userId = "1",
            name = "Amit Gupta",
            email = "amit@gmail.com",
            mobile = "9876543210",
            branch = "Computer Science",
            branchId = 1,
            year = "2020",
            job = "",
            location = "",
            birthday = "",
            linkedin = "",
            photo = "",
            totalExp = 2
        )


        composable(Routes.SPLASH_HOME)
        {
            SplashScreen(navController)

        }

        composable(Routes.LOGIN)
        {
            LoginRoute(navController)

        }
        // 🔢 OTP Screen
     composable(Routes.VERIFICATION)
     {
         AccountVerificationScreen(
             navController = navController,

         )
     }


        val pages = listOf(
            IntroPage(
                "Connect with Alumni",
                "Stay connected with batchmates, seniors, and the global Harcourtian alumni network.",
                R.drawable.first
            ),
            IntroPage(
                "Explore Opportunities",
                "Access jobs, referrals, events, and mentorship opportunities from alumni.",
                R.drawable.second
            ),
            IntroPage(
                "Give Back to Harcourtian",
                "Contribute to students, campus initiatives, and the future of Harcourtian.",
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
        composable(
            route = "${Routes.SERVICE_DETAILS}?title={title}&location={location}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("location") { type = NavType.StringType }
            )
        ) { entry ->
            val title = entry.arguments?.getString("title").orEmpty()
            val location = entry.arguments?.getString("location").orEmpty()

            ProductDetailsScreen(navController,title, location,"","")
        }

        composable(Routes.JOB_DETAILS){
            JobListingScreen(navController)
        }
        composable(Routes.SUBSCRIPTION){
            PremiumScreen(navController)
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
        composable(Routes.PRODUCT_SCREEN){
            ProductServiceDummyScreen(navController)
        }
        composable(Routes.ALMUNI_POST){
            AlumniStoriesScreen(navController)
        }
        composable("story_detail/{name}") { backStackEntry ->

            val name = backStackEntry.arguments?.getString("name")
           Log.e("Name", "Received name: $name")
            val story = alumniFeed.firstOrNull {
                it.name == name
            }
            Log.e("Name", "Received name: ${story?.name}")

            AlumniStoryDetailScreen(
                navController = navController,
                story = story   // already nullable safe
            )
        }
//        composable(Routes.ENTRY) {
//            EmergencyFeedScreen {
//                navController.navigate(Routes.REQUEST)
//            }
//        }

        composable(Routes.REQUEST) {
            EmergencyRequestForm {
                // API call → submit emergency
                navController.navigate(Routes.FEED) {
                    popUpTo(Routes.ENTRY) { inclusive = true }
                }
            }
        }

        /* ---------- FEED ---------- */
        composable(Routes.FEED) {
            EmergencyFeedScreen (navController){ emergency ->
                navController.navigate(
                    "emergency_detail/${emergency.id}"
                )
            }
        }

        /* ---------- DETAIL ---------- */
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val id =
                backStackEntry.arguments?.getString("id") ?: ""

            val emergency =
                demoEmergencyList.first { it.id == id }

            EmergencyDetailScreen(
                navController,
                emergency = emergency,
                onDonateClick = {
                    navController.navigate(
                        "emergency_donate/${emergency.id}"
                    )
                }
            )
        }

        /* ---------- DONATE ---------- */
        composable(
            Routes.DONATE,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val emergencyId =
                backStackEntry.arguments?.getString("id") ?: ""

            DonateAmountScreen(navController) { amount ->
                navController.navigate(
                    "donation_success/$amount/$emergencyId"
                ) {
                    popUpTo(Routes.FEED)
                }
            }
        }

        /* ---------- SUCCESS ---------- */
        composable(
            route = Routes.SUCCESS,
            arguments = listOf(
                navArgument("amount") { type = NavType.IntType },
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val amount =
                backStackEntry.arguments?.getInt("amount") ?: 0

            val emergencyId =
                backStackEntry.arguments?.getString("id") ?: ""

            DonationSuccessScreen(
                navController,
                amount = amount,

                onGoHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0)
                    }
                },

                onViewEmergency = {

                    navController.navigate(
                        "emergency_detail/$emergencyId"
                    )
                }
            )
        }




    }
}
