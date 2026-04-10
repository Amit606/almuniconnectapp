package com.kwh.almuniconnect

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.kwh.almuniconnect.login.LoginRoute
import com.kwh.almuniconnect.profile.ProfileScreen
import com.kwh.almuniconnect.settings.SettingsScreen
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kwh.almuniconnect.almunipost.AlumniStoriesScreen
import com.kwh.almuniconnect.almunipost.AlumniStoryDetailScreen
import com.kwh.almuniconnect.almunipost.SuccessViewModel
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.branding.ProductDetailsScreen
import com.kwh.almuniconnect.branding.ProductServiceDummyScreen
import com.kwh.almuniconnect.branding.WebViewScreen
import com.kwh.almuniconnect.emergency.DonateAmountScreen
import com.kwh.almuniconnect.emergency.DonationSuccessScreen
import com.kwh.almuniconnect.emergency.EmergencyDetailScreen
import com.kwh.almuniconnect.emergency.EmergencyFeedScreen
import com.kwh.almuniconnect.emergency.EmergencyRequestForm
import com.kwh.almuniconnect.emergency.demoEmergencyList
import com.kwh.almuniconnect.feedback.FeedbackForm
import com.kwh.almuniconnect.help.AddSocialChannelScreen
import com.kwh.almuniconnect.home.JobProfileScreen
import com.kwh.almuniconnect.jobposting.JobPostByEmailScreen
import com.kwh.almuniconnect.jobposting.jobprofile.CreateJobProfileScreen
import com.kwh.almuniconnect.login.PrivacyPolicyScreen
import com.kwh.almuniconnect.login.TermsScreen
import com.kwh.almuniconnect.morefeature.ComingSoonScreen
import com.kwh.almuniconnect.morefeature.MediaScreen
import com.kwh.almuniconnect.morefeature.MoreFeaturesScreen
import com.kwh.almuniconnect.morefeature.YoutubePlayerScreen
import com.kwh.almuniconnect.nearby.LocationPermissionScreen
import com.kwh.almuniconnect.nearby.NearbyHarcourtianScreen
import com.kwh.almuniconnect.network.AlumniBatchViewModel
import com.kwh.almuniconnect.network.AlumniBatchViewModelFactory
import com.kwh.almuniconnect.network.AlumniDto
import com.kwh.almuniconnect.network.AlumniListScreen
import com.kwh.almuniconnect.network.AlumniRepository
import com.kwh.almuniconnect.network.AlumniViewModelFactory
import com.kwh.almuniconnect.network.AppDatabase
import com.kwh.almuniconnect.network.BranchRepository
import com.kwh.almuniconnect.network.YearGridScreen
import com.kwh.almuniconnect.network.BranchScreen
import com.kwh.almuniconnect.network.BranchViewModel
import com.kwh.almuniconnect.network.BranchViewModelFactory
import com.kwh.almuniconnect.news.NewsDetailScreen
import com.kwh.almuniconnect.news.NewsListingScreen
import com.kwh.almuniconnect.profile.AlumniProfileScreen
import com.kwh.almuniconnect.subscription.PremiumScreen
import com.kwh.almuniconnect.profile.ApprovalPendingScreen
import com.kwh.almuniconnect.tallent.AddTalentScreen
import com.kwh.almuniconnect.tallent.HarcourtianTalentScreen
import com.kwh.almuniconnect.tallent.TalentDetailScreen
import com.kwh.almuniconnect.tallent.TalentViewModel
import com.kwh.almuniconnect.tallent.shareTalent
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
//            JobProfileScreen(
//                onSaveProfile = { profileData ->
//                    // Send to backend or save locally
//                    Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_LONG).show()
//                },
//                onCancel = { navController.popBackStack() }
//            )
        }
        composable(Routes.JOB_PROFILE){

            JobProfileScreen(
                onBack = { navController.popBackStack() },

            )
        }
        composable(Routes.NEARBY_HARCOURTIANS_PERMISSION) {
            LocationPermissionScreen(navController,onAllowClick = {
                navController.navigate(Routes.NEARBY_HARCOURTIANS)
            }) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
           // NearbyHarcourtianScreen(navController)
        }
        composable(Routes.NEARBY_HARCOURTIANS) {

             NearbyHarcourtianScreen(navController)
        }


        composable(Routes.TALENT_LIST) {
            HarcourtianTalentScreen(navController)

        }
        composable(Routes.ADD_TALENT_LIST) {
            AddTalentScreen(navController)

        }
        composable(
            route = "talent_detail/{talentId}",
            arguments = listOf(navArgument("talentId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->

            val viewModel: TalentViewModel = viewModel()
            val talentId = backStackEntry.arguments?.getString("talentId")
            val talent = viewModel.getTalentById(talentId)

            talent?.let {
                TalentDetailScreen(
                    navController,
                    talent = it,
                    onLikeClick = { viewModel.likeTalent(it.id) },
                    onShareClick = { shareTalent(context, it) }
                )
            }
        }

        composable(Routes.BRANCHES) {

            val context = LocalContext.current

            val database = remember {
                AppDatabase.getDatabase(context)
            }

            val dao = remember {
                database.branchDao()
            }

            val remoteConfig = remember {
                FirebaseRemoteConfig.getInstance()
            }

            val repository = remember {
                BranchRepository(dao, remoteConfig)
            }

            val viewModel: BranchViewModel = viewModel(
                factory = BranchViewModelFactory(repository)
            )

            BranchScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = "year/{branchId}/{branchShort}",
            arguments = listOf(
                navArgument("branchId") { type = NavType.IntType },
                navArgument("branchShort") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val branchId = backStackEntry.arguments?.getInt("branchId") ?: 0
            val branchShort = backStackEntry.arguments?.getString("branchShort") ?: ""

            // Create ApiService
            val apiService = remember {
                NetworkClient.createService(ApiService::class.java)
            }

            // Create Repository
            val repository = remember {
                AlumniRepository(apiService)
            }

            // Create ViewModel Factory
            val factory = remember {
                AlumniBatchViewModelFactory(repository)
            }

            // Create ViewModel
            val viewModel: AlumniBatchViewModel = viewModel(factory = factory)

            YearGridScreen(
                navController = navController,
                branchId = branchId,
                branchShort = branchShort,
                viewModel = viewModel
            )
        }
        composable(Routes.NEWS) {
            NewsListingScreen(navController)
        }



//        composable(
//            route = "news_detail/{title}/{content}/{imageUrl}/{publishedAt}"
//        ) { backStackEntry ->
//
//            val title = backStackEntry.arguments?.getString("title") ?: ""
//            val content = backStackEntry.arguments?.getString("content") ?: ""
//            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
//            val publishedAt = backStackEntry.arguments?.getString("publishedAt") ?: ""
//
//            NewsDetailScreen(
//                navController,
//                title = title,
//                description = content,
//                imageUrl = imageUrl,
//                date = publishedAt,
//                authorName = "Alumni Connect App Team" // agar pass nahi kar rahe ho
//
//            )
//        }


            // 🔹 Route 1 (API type)
            composable(
                route = "news_detail/{title}/{content}/{imageUrl}/{publishedAt}"
            ) { backStackEntry ->

                val title = backStackEntry.arguments?.getString("title") ?: ""
                val description = backStackEntry.arguments?.getString("content") ?: ""
                val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                val date = backStackEntry.arguments?.getString("publishedAt") ?: ""

                NewsDetailScreen(
                    navController,
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    date = date,
                    authorName = "Alumni Connect App Team"
                )
            }

            // 🔹 Route 2 (Local JSON type)
            composable(
                route = "news_detail_alt/{title}/{description}/{imageUrl}/{date}"
            ) { backStackEntry ->

                val title = backStackEntry.arguments?.getString("title") ?: ""
                val description = backStackEntry.arguments?.getString("description") ?: ""
                val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                val date = backStackEntry.arguments?.getString("date") ?: ""

                NewsDetailScreen(
                    navController,
                    title = title,
                    description = description,
                    imageUrl = imageUrl,
                    date = date,
                    authorName = "Alumni Connect App Team"
                )
            }






        composable("player/{videoId}") { backStackEntry ->
            val videoId = Uri.decode(
                backStackEntry.arguments?.getString("videoId") ?: ""
            )
            YoutubePlayerScreen(videoId,navController)
        }
        composable(Routes.MORE_FEATURES) {
            MoreFeaturesScreen(navController)
        }
        composable(Routes.MEDIA_FEATURE) {
            MediaScreen(navController)
        }
        composable(Routes.Internet_Splash) {
            NoInternetDialog(

            )

        }


        composable(Routes.USER_PROFILE)
        {
            val context = LocalContext.current

            val database = remember {
                AppDatabase.getDatabase(context)
            }

            val dao = remember {
                database.branchDao()
            }

            val remoteConfig = remember {
                FirebaseRemoteConfig.getInstance()
            }

            val repository = remember {
                BranchRepository(dao, remoteConfig)
            }

            val viewModel: BranchViewModel = viewModel(
                factory = BranchViewModelFactory(repository)
            )
            ProfileScreen(navController,viewModel)
        }
        composable(Routes.APPROVAL_PENDING) {
            ApprovalPendingScreen()
        }
        composable(
            route = "alumni/{branchShort}/{year}",
            arguments = listOf(
                navArgument("branchShort") {
                    type = NavType.StringType
                },
                navArgument("year") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val branchShort =
                backStackEntry.arguments?.getString("branchShort")
                    ?: return@composable

            val year =
                backStackEntry.arguments?.getInt("year")
                    ?: return@composable

            AlumniListScreen(
                navController = navController,
                branchShort = branchShort,
                year = year
            )
        }
        composable("terms") {
            TermsScreen {
                navController.popBackStack()
            }
        }

        composable("privacy") {
            PrivacyPolicyScreen {
                navController.popBackStack()
            }
        }
//        composable(Routes.NETWORK) {
//            val apiService = remember {
//                NetworkClient.createService(ApiService::class.java)
//            }
//            val repository = remember { AlumniRepository(apiService) }
//
//            val alumniViewModel: AlumniViewModel = viewModel(
//                factory = AlumniViewModelFactory(repository)
//            )
//
//            NetworkScreen(
//                navController = navController,
//                onOpenProfile = { alumni ->
//                    navController.navigate(
//                        Routes.profileRoute(alumni.alumniId)
//                    )
//                }
//            )
//        }
        composable("profile") {

            val alumni = navController
                .previousBackStackEntry
                ?.savedStateHandle
                ?.get<AlumniDto>("alumni")

            alumni?.let {
                AlumniProfileScreen(
                    alumni = it,
                    navController = navController
                )
            }
        }



        composable(Routes.SPLASH_HOME)
        {
            SplashScreen(navController)

        }
        composable(Routes.FEEDBACK)
        {
            FeedbackForm(navController)

        }
        composable(Routes.COMING_SOON)
        {
            ComingSoonScreen(
                navController,
                onNotifyClick = {

                    if (isSubscribed(context)) {
                        Toast.makeText(
                            context,
                            "Already subscribed ✅",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@ComingSoonScreen
                    }

                    FirebaseMessaging.getInstance().subscribeToTopic("coming_soon")
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                setSubscribed(context) // ✅ save state

                                Toast.makeText(
                                    context,
                                    "You’ll be notified 🚀",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            )
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
                "Once a Harcourtian, Always a Harcourtian",
                "Your journey at Harcourt never truly ends. Reconnect with the people who shared your classrooms, hostels, memories, and dreams.",
                R.drawable.ic_first
            ),

            IntroPage(
                "Reconnect with the Alumni Network",
                "Stay connected with batchmates, seniors, juniors, and Harcourtians across the globe. Build meaningful relationships that last beyond campus.",
                R.drawable.ic_second
            ),

            IntroPage(
                "Discover Talent, Products & Services",
                "Explore startups, innovations, products, and professional services created by fellow Harcourtians. Support alumni talent and collaborate together.",
                R.drawable.ic_third
            ),

            IntroPage(
                "Opportunities & Mentorship",
                "Find jobs, referrals, internships, and mentorship from experienced alumni. Grow your career with guidance from the Harcourtian network.",
                R.drawable.ic_fourth
            ),

            IntroPage(
                "Give Back to the Community",
                "Share opportunities, guide students, support alumni ventures, and help strengthen the Harcourtian legacy for the next generation.",
                R.drawable.ic_fifth
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
        composable(Routes.ADD_SOCIAL_CHANNEL) {
            AddSocialChannelScreen(navController)
        }


        // 🟩 Login
//
        composable(
            route = "webview/{url}"
        ) { backStackEntry ->

            val url = Uri.decode(backStackEntry.arguments?.getString("url") ?: "")

            WebViewScreen(
                url = url,
                navController = navController
            )
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

        composable(
            route = "${Routes.EVENT_DETAILS}?title={title}&date={date}&location={location}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("location") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }

            )
        ) { entry ->
            val title = entry.arguments?.getString("title").orEmpty()
            val location = entry.arguments?.getString("location").orEmpty()
            val date = entry.arguments?.getString("date").orEmpty()


            EventDetailsScreen(navController,title, location,date,"")
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
            jobId?.let {
                JobDetailScreen(
                    navController = navController,
                    jobId = it
                )
            }
        }

        composable(Routes.JOB_POST){
            JobPostByEmailScreen(navController)
        }
        composable(Routes.PRODUCT_SCREEN){
            ProductServiceDummyScreen(navController)
        }
        composable(Routes.ALMUNI_POST){
            AlumniStoriesScreen(navController)
        }
        composable("story_detail/{name}") { backStackEntry ->

            val name = backStackEntry.arguments?.getString("name")

            val viewModel: SuccessViewModel = viewModel()
            val alumniList by viewModel.alumniList.collectAsState()

            val story = alumniList.firstOrNull { it.name == name }

            AlumniStoryDetailScreen(
                navController = navController,
                story = story
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
fun isSubscribed(context: Context): Boolean {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("coming_soon_subscribed", false)
}

fun setSubscribed(context: Context) {
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("coming_soon_subscribed", true).apply()
}
fun saveUserConsent(userId: String) {

    val db = FirebaseFirestore.getInstance()

    val consent = UserConsent(
        userId = userId,
        consentGiven = true,
        timestamp = System.currentTimeMillis(),
        policyVersion = "v1.0",
        device = android.os.Build.MODEL
    )

    db.collection("users")
        .document(userId)
        .collection("consents")
        .add(consent)
        .addOnSuccessListener {
            Log.d("CONSENT", "Consent saved successfully")
        }
        .addOnFailureListener {
            Log.e("CONSENT", "Error saving consent", it)
        }
}
data class UserConsent(
    val userId: String = "",
    val consentGiven: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val policyVersion: String = "v1.0",
    val device: String = android.os.Build.MODEL
)