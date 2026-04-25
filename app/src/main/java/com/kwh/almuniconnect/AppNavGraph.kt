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
import com.google.firebase.firestore.FieldValue
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
import com.kwh.almuniconnect.login.PrivacyPolicyScreen
import com.kwh.almuniconnect.login.TermsScreen
import com.kwh.almuniconnect.morefeature.ComingSoonScreen
import com.kwh.almuniconnect.morefeature.JobProfileComingSoonScreen
import com.kwh.almuniconnect.morefeature.MediaScreen
import com.kwh.almuniconnect.morefeature.NearbyComingSoonScreen
import com.kwh.almuniconnect.morefeature.YoutubePlayerScreen
import com.kwh.almuniconnect.nearby.LocationPermissionScreen
import com.kwh.almuniconnect.nearby.NearbyHarcourtianScreen
import com.kwh.almuniconnect.network.AlumniBatchViewModel
import com.kwh.almuniconnect.network.AlumniBatchViewModelFactory
import com.kwh.almuniconnect.network.AlumniDto
import com.kwh.almuniconnect.network.AlumniListScreen
import com.kwh.almuniconnect.network.AlumniRepository
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
import androidx.core.content.edit
import com.kwh.almuniconnect.analytics.AnalyticsManager
import com.kwh.almuniconnect.help.SocialCommunityScreen
import com.kwh.almuniconnect.morefeature.MoreFeaturesBottomSheet
import com.kwh.almuniconnect.nearby.NearAlumni
import com.kwh.almuniconnect.nearby.NearAlumniProfile
import com.kwh.almuniconnect.nearby.PermissionInfoScreen

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

        }
        composable(Routes.JOB_PROFILE){
            AnalyticsManager.logScreen(context,"JOB_PROFILE")

            JobProfileScreen(
                onBack = { navController.popBackStack() },

            )
        }
        composable(Routes.JOB_PROFILE_COMMING_SOON)
        {
            AnalyticsManager.logScreen(context,"JOB_PROFILE_COMMING_SOON")

            JobProfileComingSoonScreen(navController, onNotifyClick =
                {
                    subscribeToJobProfile(context)

                }
                )
        }
        //NearbyComingSoonScreen
        composable(Routes.NEAR_BY_COMMING_SOON)
        {
            AnalyticsManager.logScreen(context,"NEAR_BY_COMMING_SOON")

            NearbyComingSoonScreen(navController, onNotifyClick =
                {
                    subscribeToNearbyFeature(context)

                }
            )
        }

        composable(Routes.NEARBY_HARCOURTIANS_PERMISSION) {
            AnalyticsManager.logScreen(context,"NEARBY_HARCOURTIANS_PERMISSION")

            LocationPermissionScreen(
                navController,
                onAllowClick = {
                    navController.navigate(Routes.NEARBY_HARCOURTIANS) {
                        popUpTo(Routes.NEARBY_HARCOURTIANS_PERMISSION) { inclusive = true }
                    }
                },
                onSkipClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.NEARBY_HARCOURTIANS) {
            AnalyticsManager.logScreen(context,"NEARBY_HARCOURTIANS")

            NearbyHarcourtianScreen(navController)
        }


        composable(Routes.TALENT_LIST) {
            AnalyticsManager.logScreen(context,"TALENT_LIST")

            HarcourtianTalentScreen(navController)

        }
        composable(Routes.ADD_TALENT_LIST) {
            AnalyticsManager.logScreen(context,"ADD_TALENT_LIST")

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

            AnalyticsManager.logScreen(context,"BRANCHES")

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
            AnalyticsManager.logScreen(context,"YearGridScreen")

            YearGridScreen(
                navController = navController,
                branchId = branchId,
                branchShort = branchShort,
                viewModel = viewModel
            )
        }
        composable(Routes.NEWS) {
            AnalyticsManager.logScreen(context,"NEWS")

            NewsListingScreen(navController)
        }



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
            AnalyticsManager.logScreen(context,"YoutubePlayerScreen")

            YoutubePlayerScreen(videoId,navController)
        }
        composable(Routes.MORE_FEATURES) {
            AnalyticsManager.logScreen(context,"MORE_FEATURES")

            MoreFeaturesBottomSheet(navController)
        }
        composable(Routes.MEDIA_FEATURE) {
            AnalyticsManager.logScreen(context,"MEDIA_FEATURE")
            MediaScreen(navController)
        }
        composable(Routes.Internet_Splash) {
            NoInternetDialog(

            )

        }


        composable(Routes.USER_PROFILE)
        {
            AnalyticsManager.logScreen(context,"USER_PROFILE")

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
            AnalyticsManager.logScreen(context,"APPROVAL_PENDING")

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
            AnalyticsManager.logScreen(context,"terms")

            TermsScreen {
                navController.popBackStack()
            }
        }

        composable("privacy") {
            AnalyticsManager.logScreen(context,"privacy")

            PrivacyPolicyScreen {
                navController.popBackStack()
            }
        }

        composable("profile") {
            AnalyticsManager.logScreen(context,"profile")


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
        composable(Routes.NEAR_ALUMNI_PROFILE) {
            AnalyticsManager.logScreen(context,"NEAR_ALUMNI_PROFILE")

                val alumni = navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<NearAlumni>("nearalumni")

                alumni?.let {
                    NearAlumniProfile(
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
            AnalyticsManager.logScreen(context,"FEEDBACK")

            FeedbackForm(navController)

        }
        composable(Routes.COMING_SOON)
        {
            AnalyticsManager.logScreen(context,"COMING_SOON")

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
            AnalyticsManager.logScreen(context,"LOGIN")

            LoginRoute(navController)

        }
        // 🔢 OTP Screen
     composable(Routes.VERIFICATION)
     {
         AnalyticsManager.logScreen(context,"VERIFICATION")

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
            AnalyticsManager.logScreen(context,"INTRO")

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
            AnalyticsManager.logScreen(context,"EVENTS")

            EventsScreen(navController)
        }
        composable(Routes.WHATSUP_CHANNEL) {
            AnalyticsManager.logScreen(context,"WHATSUP_CHANNEL")

            SocialCommunityScreen(navController)
        }
        composable(Routes.ADD_SOCIAL_CHANNEL) {
            AnalyticsManager.logScreen(context,"ADD_SOCIAL_CHANNEL")

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
            AnalyticsManager.logScreen(context,"HOME")

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
            AnalyticsManager.logScreen(context,"SETTINGS")

            SettingsScreen(
                navController = navController
            )
        }
        composable(Routes.HELP_SUPPORTS) {
            AnalyticsManager.logScreen(context,"HELP_SUPPORTS")

            HelpSupportScreen(navController) }
        composable(Routes.ABOUT_US) {
            AnalyticsManager.logScreen(context,"ABOUT_US")


            AboutAlumniConnectScreen(navController) }

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

            AnalyticsManager.logScreen(context,"EventDetailsScreen")

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
            AnalyticsManager.logScreen(context,"JOB_DETAILS")

            JobListingScreen(navController)
        }
        composable(Routes.SUBSCRIPTION){
            AnalyticsManager.logScreen(context,"SUBSCRIPTION")

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
            AnalyticsManager.logScreen(context,"JOB_POST")

            JobPostByEmailScreen(navController)
        }
        composable(Routes.PRODUCT_SCREEN){
            AnalyticsManager.logScreen(context,"PRODUCT_SCREEN")

            ProductServiceDummyScreen(navController)
        }
        composable(Routes.ALMUNI_POST){
            AnalyticsManager.logScreen(context,"ALMUNI_POST")

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
            AnalyticsManager.logScreen(context,"FEED")

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
        composable(Routes.LOCATION_PERMISSION_INFO) {
            AnalyticsManager.logScreen(context,"LOCATION_PERMISSION_INFO")

            PermissionInfoScreen(navController)
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

fun subscribeToJobProfile(context: Context) {
    try {

        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isSubscribed = prefs.getBoolean("job_profile_subscribed", false)

        // ✅ Already subscribed check
        if (isSubscribed) {
            Toast.makeText(
                context,
                "✅ You are already subscribed!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        FirebaseMessaging.getInstance().subscribeToTopic("job_profile")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // 🔥 Save locally to prevent duplicate
                    prefs.edit() { putBoolean("job_profile_subscribed", true) }

                    // 🔥 Save click in Firestore (only once)
                    val docRef = FirebaseFirestore.getInstance()
                        .collection("feature_interest")
                        .document("job_profile")

                    docRef.get().addOnSuccessListener { document ->
                        if (document.exists()) {

                            // ✅ Increment existing
                            docRef.update("count", FieldValue.increment(1))

                        } else {

                            // ✅ First time create
                            docRef.set(mapOf("count" to 1))
                        }
                    }
                    Toast.makeText(
                        context,
                        "🔔 You will be notified when Job Profile goes live!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        context,
                        "❌ Subscription failed. Try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    } catch (ex: Exception) {
        Log.e("SUBSCRIPTION", "Error subscribing to topic", ex)
    }
}
fun subscribeToNearbyFeature(context: Context) {
    try {

        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isSubscribed = prefs.getBoolean("nearby_subscribed", false)

        // ✅ Already subscribed check
        if (isSubscribed) {
            Toast.makeText(
                context,
                "✅ You are already subscribed!",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        FirebaseMessaging.getInstance().subscribeToTopic("nearby_feature")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // 🔥 Save locally
                    prefs.edit().putBoolean("nearby_subscribed", true).apply()

                    // 🔥 Update Firestore count
                    val docRef = FirebaseFirestore.getInstance()
                        .collection("feature_interest")
                        .document("nearby")

                    docRef.get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            docRef.update("count", FieldValue.increment(1))
                        } else {
                            docRef.set(mapOf("count" to 1))
                        }
                    }

                    Toast.makeText(
                        context,
                        "📍 You will be notified when Nearby goes live!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        context,
                        "❌ Subscription failed. Try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    } catch (ex: Exception) {
        Log.e("SUBSCRIPTION", "Error subscribing to nearby", ex)
    }
}