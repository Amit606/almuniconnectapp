// HomeScreen.kt
// Jetpack Compose Home screen for "AlumniConnect" app
// Features: top app bar with search & notifications, banner, horizontal lists (Events, Jobs), feed (alumni posts), FAB, and bottom navigation.

package com.kwh.almuniconnect.home
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.AnalyticsEvent
import com.kwh.almuniconnect.analytics.AnalyticsManager
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.jobposting.JobAPost
import com.kwh.almuniconnect.permission.RequestNotificationPermission
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
import com.kwh.almuniconnect.utils.encodeRoute
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.kwh.almuniconnect.almunipost.SuccessViewModel
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.branding.ProductServiceViewModel
import com.kwh.almuniconnect.evetns.Event
import com.kwh.almuniconnect.evetns.EventsUiState
import com.kwh.almuniconnect.evetns.EventsViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: SuccessViewModel=viewModel(),
    pviewModel: ProductServiceViewModel = viewModel(),

    onOpenProfile: () -> Unit = {},
    onOpenMessages: () -> Unit = {},
    onOpenEventDetails: (Event) -> Unit = {},
    onOpenJobDetails: (JobAPost) -> Unit = {},
    onCreatePost: () -> Unit = {},
    modifier: Modifier = Modifier,
) {


    val context = LocalContext.current
    var totalAlumni by remember { mutableStateOf(0L) }
    var activeUsers by remember { mutableStateOf(0L) }


    TrackScreen("home_screen")
    var showExitDialog by remember { mutableStateOf(false) }
    val activity = LocalActivity.current

    val alumniList by viewModel.alumniList.collectAsState()
    val bottomBarState = remember { mutableStateOf(BottomNavItem.Home) }
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(
        initial = UserLocalModel()
    )
    val products by pviewModel.products.collectAsState()

    val viewModel: NewsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        pviewModel.loadProducts()
    }
    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("stats")
            .document("app_stats")
            .addSnapshotListener { snapshot, error ->

                if (error != null) return@addSnapshotListener

                if (snapshot != null && snapshot.exists()) {

                    val total = snapshot.getLong("total_users") ?: 0
                    val active = snapshot.getLong("active_users") ?: 0

                    totalAlumni = total
                    activeUsers = active
                }
            }
    }

    RequestNotificationPermission(
        onPermissionGranted = {
            // 🔔 Notifications enabled
        },
        onPermissionDenied = {
            // 🚫 User denied (show snackbar or ignore)
        }
    )
    // Handle back press
    BackHandler {
        showExitDialog = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(

                title = {
                    Text(
                        "Home",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 20.sp
                        )
                    )
                },

                // 👈 LEFT SIDE PROFILE
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.USER_PROFILE)
                        },
                        modifier = Modifier.padding(start = 12.dp) // 👈 LEFT SPACE

                    ) {
                        if (user.photo.isNotEmpty()) {
                            AsyncImage(
                                model = user.photo,
                                contentDescription = "Profile photo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.man),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                },

                // 👉 RIGHT SIDE ACTIONS
                actions = {

                    // 🎤 Mic
                    IconButton(
                        onClick = { navController.navigate(Routes.TALENT_LIST) }
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            tint = Color.DarkGray,
                            contentDescription = "Mic"
                        )
                    }

                    // ⚙️ Settings
                    IconButton(
                        onClick = { navController.navigate(Routes.SETTINGS) }
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },

        bottomBar = {
            BottomAppBarWithNav(selected = bottomBarState.value) { selected ->
                bottomBarState.value = selected
                when (selected) {
                    BottomNavItem.Home -> {}
                    BottomNavItem.Network -> {
                        navController.navigate(Routes.BRANCHES)
                    }
                    BottomNavItem.JOBS -> onOpenMessages()
                    BottomNavItem.ChANNEL -> {navController.navigate(Routes.WHATSUP_CHANNEL)}
                    BottomNavItem.More -> {navController.navigate(Routes.MORE_FEATURES)}
                }
            }
        },
        contentColor = Color.White

    ) { paddingValues ->
        // Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                val viewModel = remember {
                    HomeViewModel(
                        context = context,
                        authApi = NetworkClient.createService(ApiService::class.java)
                    )
                }

                val bannerImages = viewModel.banners
                val isLoading = viewModel.isLoading
                val context = LocalContext.current

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else if (bannerImages.isNotEmpty()) {

                    val pagerState = rememberPagerState(
                        pageCount = { bannerImages.size }
                    )

                    // ✅ Auto scroll SAFE (restarts only when list changes)
                    LaunchedEffect(bannerImages.size) {
                        if (bannerImages.size > 1) {
                            while (true) {
                                delay(3000)
                                val nextPage =
                                    (pagerState.currentPage + 1) % bannerImages.size
                                pagerState.animateScrollToPage(nextPage)
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp),
                            pageSpacing = 12.dp
                        ) { page ->

                            // ✅ Safe URL handling
                            val imageUrl = bannerImages
                                .getOrNull(page)
                                ?.takeIf { it.isNotBlank() }

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(imageUrl)
                                        .crossfade(true)
                                        .placeholder(R.drawable.ic_news)
                                        .error(R.drawable.ic_services)
                                        .fallback(R.drawable.ic_alumni)
                                        .listener(
                                            onError = { _, result ->
                                                Log.e(
                                                    "BannerError",
                                                    "Failed URL: $imageUrl - ${result.throwable.message}"
                                                )
                                            }
                                        )
                                        .build(),
                                    contentDescription = "Banner",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        PagerDotsIndicator(
                            pagerState = pagerState,
                            activeColor = Color.Black,
                            inactiveColor = Color.Gray
                        )
                    }
                }
            }

            item {
                AlumniWelcomeHeader(
                    userName = user.name.ifEmpty { "Harcourtian" },
                    nearbyCount = alumniList.size
                )
            }

            item {
                AnimatedVisibility(visible = true) {
                    AlumniCountBanner(totalAlumni, 20,true)
                }
            }
            // ❤️ Welcome Header



            // Events section
            item {
                SectionTitle(
                    title = "Relive Your College Moments ✨",
                    actionText = "Explore",
                    onAction = {
                        AnalyticsManager.logEvent(context,
                            AnalyticsEvent.ScreenView("events_view_all")
                        )

                        navController.navigate(Routes.EVENTS)
                    }
                )
            }
            item {

                val viewModel: EventsViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                LaunchedEffect(Unit) {
                    viewModel.loadEvents()
                }
                when (uiState) {

                    is EventsUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is EventsUiState.Success -> {

                        val events = (uiState as EventsUiState.Success).events

                        if (events.isNotEmpty()) {

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(events) { event ->
                                    EventCard(
                                        event = event,
                                        onClick = {
                                            navController.navigate(
                                                "${Routes.EVENT_DETAILS}?title=${event.title.encodeRoute()}&description=${event.description.encodeRoute()}&date=${event.startAt.encodeRoute()}&location=${event.location.encodeRoute()}"
                                            )
                                        }
                                    )
                                }
                            }

                        } else {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No Upcoming Events",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    is EventsUiState.Error -> {
                        Text(
                            text = "No Upcoming Events",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )                    }
                }
            }


            // Jobs section
            item {
                SectionTitle(title = "Product & Services", actionText = "Discover", onAction = {
                    AnalyticsManager.logEvent(context,
                        AnalyticsEvent.ScreenView("product_view_all")
                    )
                    navController.navigate(Routes.PRODUCT_SCREEN)
                })
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(products) { item ->
                        ProductServiceCardNew(item,
                            onClick =
                                {
                                    navController.navigate("webview/${Uri.encode(item.link)}")
                                }
                        )
                    }
                }
            }

            // Feed
            item {
                SectionTitle(title = "Inspiration from Harcourtians \uD83D\uDE80", actionText = "Explore", onAction = {
                    AnalyticsManager.logEvent(context,
                        AnalyticsEvent.ScreenView("alumni_posts_view_all")
                    )
                    navController.navigate(Routes.ALMUNI_POST)

                })
            }

            item {

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = alumniList,
                        key = { it.name }   // performance + stable key
                    ) { post ->

                        AlumniPost(
                            post = post,
                            onClick = {

                                AnalyticsManager.logEvent(context,
                                    AnalyticsEvent.ScreenView(
                                        "story_clicked_${post.name}"
                                    )
                                )

                                navController.navigate(
                                    "story_detail/${post.name}"
                                )
                            }
                        )
                    }
                }
            }

            // News
            item {
                SectionTitle(
                    title = "News",
                    actionText = "More News",
                    onAction = {
                        navController.navigate(Routes.NEWS)
                    }
                )
            }

            when {
                state.isLoading -> {
                    item {
                        Text("Loading...")
                    }
                }

                state.error != null -> {
                    item {
                        Text("Error: ${state.error}")
                    }
                }

                state.news.isEmpty() -> {
                    item {
                        Text("No news available")
                    }
                }

                else -> {
                    items(state.news) { post ->
                        AlumniNews(
                            post = post,
                            onClick = {
                                navController.navigate(
                                    "news_detail_alt/${Uri.encode(post.title)}/" +
                                            "${Uri.encode(post.description)}/" +
                                            "${Uri.encode(post.imageUrl)}/" +
                                            "${Uri.encode(post.date)}"
                                )
                               // navController.navigate(Routes.NEWS)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }



        }
        // Exit dialog
        if (showExitDialog) {

            CommonActionBottomSheet(
                showDialog = showExitDialog,
                title = "Exit App",
                description = "Are you sure you want to exit?",
                confirmText = "Exit",
                onDismiss = { showExitDialog = false },
                onConfirm = { activity?.finish() }
            )

        }

    }
@Composable
fun AlumniWelcomeHeader(userName: String, nearbyCount: Int) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),   // ✅ Full width
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // ✅ Center content
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Welcome back, $userName ❤️",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                textAlign = TextAlign.Center // ✅ Center text
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$nearbyCount alumni near you 👀",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PagerDotsIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Black,
    inactiveColor: Color = Color.LightGray
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { index ->
            val isSelected = pagerState.currentPage == index

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (isSelected) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (isSelected) activeColor else inactiveColor
                    )
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String, actionText: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium,color =Color.Black)
        Text(actionText, style = MaterialTheme.typography.titleMedium,color =Color.Black, modifier = Modifier.clickable(onClick = onAction))
    }
}

// Event card






// Bottom navigation items
enum class BottomNavItem { Home, Network, JOBS,ChANNEL,More }

@Composable
fun BottomAppBarWithNav(
    selected: BottomNavItem,
    onSelect: (BottomNavItem) -> Unit
) {
    BottomAppBar(
        containerColor = Color.White,
        contentColor = Color.White){
    NavigationBar {
        NavigationBarItem(
            selected = selected == BottomNavItem.Home,
            onClick = { onSelect(BottomNavItem.Home) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E3A8A),
                selectedTextColor = Color(0xFF1E3A8A),
                indicatorColor = Color(0xFFE0E7FF)
            ),
            icon = {
                Icon(
                    if (selected == BottomNavItem.Home)
                        Icons.Filled.Home
                    else
                        Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.Network,
            onClick = { onSelect(BottomNavItem.Network) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E3A8A),
                selectedTextColor = Color(0xFF1E3A8A),
                indicatorColor = Color(0xFFE0E7FF)
            ),
            icon = {
                Icon(
                    if (selected == BottomNavItem.Network)
                        Icons.Filled.People
                    else
                        Icons.Outlined.People,
                    contentDescription = "Network"
                )
            },
            label = { Text("Network") }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.JOBS,
            onClick = { onSelect(BottomNavItem.JOBS) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E3A8A),
                selectedTextColor = Color(0xFF1E3A8A),
                indicatorColor = Color(0xFFE0E7FF)
            ),
            icon = {
                Icon(
                    if (selected == BottomNavItem.JOBS)
                        Icons.Filled.Work
                    else
                        Icons.Outlined.Work,
                    contentDescription = "Jobs"
                )
            },
            label = { Text("Jobs") }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.ChANNEL,
            onClick = { onSelect(BottomNavItem.ChANNEL) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E3A8A),
                selectedTextColor = Color(0xFF1E3A8A),
                indicatorColor = Color(0xFFE0E7FF)
            ),
            icon = {
                Icon(
                    if (selected == BottomNavItem.ChANNEL)
                        Icons.Filled.DynamicFeed
                    else
                        Icons.Outlined.DynamicFeed,
                    contentDescription = "Feed"
                )
            },
            label = { Text("Feed") }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.More,
            onClick = { onSelect(BottomNavItem.More) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1E3A8A),
                selectedTextColor = Color(0xFF1E3A8A),
                indicatorColor = Color(0xFFE0E7FF)
            ),
            icon = {
                Icon(
                    if (selected == BottomNavItem.More)
                        Icons.Filled.Menu
                    else
                        Icons.Outlined.Menu,
                    contentDescription = "More"
                )
            },
            label = { Text("More") }
        )
    }}
}

data class UniversityNews(
    val id: String,

    val title: String,
    val description: String,
    val date: String,
    val imageUrl: String?,
    val category: String?,
    val authorName: String?,
    val authorImage: String?
)
data class HProduct(
    val id: String,
    val productName: String,
    val category: ProductCategory,
    val tagline: String,
    val founderName: String,
    val location: String,
    val isActive: Boolean = true
)
enum class ProductCategory {
    ALUMNI,
    CAREER,
    EDUCATION,
    NETWORKING
}








