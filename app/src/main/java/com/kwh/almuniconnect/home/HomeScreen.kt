// HomeScreen.kt
// Jetpack Compose Home screen for "AlumniConnect" app
// Features: top app bar with search & notifications, banner, horizontal lists (Events, Jobs), feed (alumni posts), FAB, and bottom navigation.

package com.kwh.almuniconnect.home
import android.app.Activity
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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.almunipost.AlumniStory
import com.kwh.almuniconnect.almunipost.alumniFeed
import com.kwh.almuniconnect.analytics.AnalyticsEvent
import com.kwh.almuniconnect.analytics.AnalyticsManager
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.jobposting.JobAPost
import com.kwh.almuniconnect.jobposting.dummyJobPosts
import com.kwh.almuniconnect.network.NetworkScreen
import com.kwh.almuniconnect.permission.RequestNotificationPermission
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
import com.kwh.almuniconnect.utils.encodeRoute
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,

    onOpenProfile: () -> Unit = {},
    onOpenMessages: () -> Unit = {},
    onOpenEventDetails: (Event) -> Unit = {},
    onOpenJobDetails: (JobAPost) -> Unit = {},
    onCreatePost: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val bannerImages = listOf(
        "https://hbtu.ac.in/wp-content/uploads/2024/07/esummit2.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2DeC1-buz32t5m1PzWA9lzU2RiJoBZY0z6w&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkyoDGJ-JKnlOGS9nMXtD9zYzYWbsBWLKenw&s",
        "https://farelabs.com/wp-content/uploads/2025/02/WhatsApp-Image-2025-02-15-at-2.31.02-PM-1024x766.png",
        "https://hbtu.ac.in/wp-content/uploads/2024/09/MoU_FARE-Labs.jpg"
    )

    val context = LocalContext.current
    TrackScreen("home_screen")
    var showExitDialog by remember { mutableStateOf(false) }
    val activity = LocalActivity.current


    val bottomBarState = remember { mutableStateOf(BottomNavItem.Home) }
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(
        initial = UserLocalModel()
    )
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
                            fontSize = 20.sp   // 👈 change size here
                        )
                        )
                },
                actions = {
//                    IconButton(onClick = { /* open notifications */ }) {
//                        Icon(
//                            Icons.Default.Notifications,
//                            tint = Color.Blue, // Gold highlight
//                            contentDescription = "Notifications"
//                        )
//                    }
                    IconButton(onClick = {  navController.navigate(Routes.FEED) }) {
                        Icon(
                            Icons.Default.Emergency,
                            tint = Color.Red, // Gold highlight
                            contentDescription = "Emergency Help"
                        )
                    }
                    IconButton(onClick = { navController.navigate(Routes.SUBSCRIPTION) }) {
                        Icon(
                            Icons.Default.WorkspacePremium,
                            tint = Color.Blue, // Gold highlight
                            contentDescription = "Premium Access"
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(Routes.USER_PROFILE)
                    }) {
                        if (user.photo.isNotEmpty()) {
                            AsyncImage(
                                model = user.photo,
                                contentDescription = "Profile photo",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.girl),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),

            )
        },

        bottomBar = {
            BottomAppBarWithNav(selected = bottomBarState.value) { selected ->
                bottomBarState.value = selected
                when (selected) {
                    BottomNavItem.Home -> {}
                    BottomNavItem.Network -> {
                        navController.navigate(Routes.NETWORK)
                    }
                    BottomNavItem.JOBS -> onOpenMessages()
                    BottomNavItem.ChANNEL -> {navController.navigate(Routes.WHATSUP_CHANNEL)}

                    BottomNavItem.Settings -> {navController.navigate(Routes.SETTINGS)}
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

                val pagerState = rememberPagerState(pageCount = { bannerImages.size })

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

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(bannerImages[page])
                                    .crossfade(true)
                                    .error(R.drawable.newggg)        // ❌ broken URL
                                    .placeholder(R.drawable.newggg) // ⏳ loading
                                    .fallback(R.drawable.newggg)    // ❓ null data
                                    .build(),
                                contentDescription = "Banner",
                                contentScale = ContentScale.Crop,
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


            // Events section
            item {
                SectionTitle(
                    title = "Upcoming Events",
                    actionText = "View All",
                    onAction = {
                        AnalyticsManager.logEvent(
                            AnalyticsEvent.ScreenView("events_view_all")
                        )

                        navController.navigate(Routes.EVENTS)
                    }
                )
            }

            item {
                val sampleEvents = sampleEvents()
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleEvents) { event ->
                        EventCard(event = event, onClick = {
                            AnalyticsManager.logEvent(
                                AnalyticsEvent.ScreenView("events_clicked_${event.id}")
                            )
                            navController.navigate(
                            "${Routes.EVENT_DETAILS}?title=${event.title.encodeRoute()}&location=${event.location.encodeRoute()}"
                        ) })
                    }
                }
            }

            // Events section
            item {
                SectionTitle(
                    title = "Products & Services",
                    actionText = "View All",
                    onAction = {
                        AnalyticsManager.logEvent(
                            AnalyticsEvent.ScreenView("Services_view_all")
                        )
                        navController.navigate(Routes.PRODUCT_SCREEN)
                    }
                )
            }

            item {
                val sampleEvents = getDummyProducts()
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleEvents) { event ->
                        ProductCard(event = event, onClick = {
                            AnalyticsManager.logEvent(
                                AnalyticsEvent.ScreenView("services_clicked_${event.productName}")
                            )
                           navController.navigate(Routes.PRODUCT_SCREEN)


                        }
                        )
                    }
                }
            }

            // Jobs section
            item {
                SectionTitle(title = "Jobs & Opportunities", actionText = "More", onAction = {
                    AnalyticsManager.logEvent(
                        AnalyticsEvent.ScreenView("jobs_view_all")
                    )
                    navController.navigate(Routes.JOB_DETAILS)
                })
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(dummyJobPosts) { job ->
                        JobMiniCard(job = job, onClick = {
                            AnalyticsManager.logEvent(
                                AnalyticsEvent.ScreenView("jobs_clicked_${job.title}")
                            )
                            navController.navigate("job_details/${job.jobId}")
                        })
                    }
                }
            }

            // Feed
            item {
                SectionTitle(title = "Alumni Stories & Achievements", actionText = "View All", onAction = {
                    AnalyticsManager.logEvent(
                        AnalyticsEvent.ScreenView("alumni_posts_view_all")
                    )
                    navController.navigate(Routes.ALMUNI_POST)

                })
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(alumniFeed) { post ->
                        AlumniPost(post = post, onClick={
                            AnalyticsManager.logEvent(
                                AnalyticsEvent.ScreenView("story_clicked_${post.name}")
                            )
                            navController.navigate("story_detail/${post.name}")

                        })
                    }
                }
            }


            // News
            item {
                SectionTitle(title = "News", actionText = "", onAction = {
                    navController.navigate(Routes.ALMUNI_POST)
                })
            }

            items(sampleNews()) { post ->
                AlumniNews(post = post,onClick = {navController.navigate(Routes.NEWS)})
                Spacer(modifier = Modifier.height(8.dp)) // 👈 space below title

            }



        }
        // Exit dialog
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text("Exit App") },
                text = { Text("Are you sure you want to exit?") },
                confirmButton = {
                    TextButton(onClick = {
                        showExitDialog = false
                        activity?.finish()
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("No")
                    }
                }
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
enum class BottomNavItem { Home, Network, JOBS,ChANNEL, Settings }

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
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.Network,
            onClick = { onSelect(BottomNavItem.Network) },
            icon = { Icon(Icons.Default.Group, contentDescription = null) },
            label = { Text("Alumni") }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.JOBS,
            onClick = { onSelect(BottomNavItem.JOBS) },
            icon = { Icon(Icons.Default.PostAdd, contentDescription = null) },
            label = { Text("Jobs") }
        )
        NavigationBarItem(
            selected = selected == BottomNavItem.ChANNEL,
            onClick = { onSelect(BottomNavItem.ChANNEL) },
            icon = { Icon(Icons.Default.Whatsapp, contentDescription = null) },
            label = { Text("Channel") }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.Settings,
            onClick = { onSelect(BottomNavItem.Settings) },
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("Settings") }
        )
    }}
}

// Sample models + data
data class Event(val id: String, val title: String, val date: String, val location: String)

private fun sampleEvents() = listOf(
    Event("1", "MCA Almuni Meet 2026", "Feb 22, 2026", "Delhi/NCR"),
    Event("2", "Delhi NCR - Holi Milan", "March 15", "Noida "),
)




private fun sampleNews(): List<UniversityNews> {
    return listOf(
        UniversityNews(
            id = "1",
            title = "Harcourtian MCA Alumni Meet 2026 Announced 🎉",
            description = "The grand Harcourtian MCA Alumni Meet 2026 will be held on 22 February 2026. Registrations are open now.",
            date = "1 Jan 2026"
        )

    )
}
data class UniversityNews(
    val id: String,
    val title: String,
    val description: String,
    val date: String
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
fun getDummyProducts() = listOf(
    HProduct(
        id = "1",
        productName = "HBTU Alumni Mart",
        category = ProductCategory.ALUMNI,
        tagline = "Discover, connect, and grow with HBTU alumni worldwide",
        founderName = "HBTU Alumni Association",
        location = "Kanpur, Uttar Pradesh, India • Global Network"
    ),
    HProduct(
        id = "2",
        productName = "Harcourtian Jobs",
        category = ProductCategory.CAREER,
        tagline = "Jobs and referrals from trusted alumni",
        founderName = "Harcourtian Team",
        location = "Bengaluru"
    ),
    HProduct(
        id = "3",
        productName = "Align My Career",
        category = ProductCategory.CAREER,
        tagline = "Career guidance for students and professionals",
        founderName = "Ankit Gupta",
        location = "Delhi"
    ),
    HProduct(
        id = "4",
        productName = "AlumBridge",
        category = ProductCategory.ALUMNI,
        tagline = "Reuniting alumni through shared journeys",
        founderName = "Harcourtian Ventures",
        location = "Mumbai"
    ),
    HProduct(
        id = "5",
        productName = "MentorNet",
        category = ProductCategory.EDUCATION,
        tagline = "Mentorship from experienced professionals",
        founderName = "Rohit Verma",
        location = "Pune"
    ),
    HProduct(
        id = "6",
        productName = "CampusConnect",
        category = ProductCategory.EDUCATION,
        tagline = "Bridging students and alumni",
        founderName = "Harcourtian Innovation",
        location = "Noida"
    ),
    HProduct(
        id = "7",
        productName = "Referra",
        category = ProductCategory.NETWORKING,
        tagline = "Smart employee referral platform",
        founderName = "Amit Sharma",
        location = "Gurugram"
    ),
    HProduct(
        id = "8",
        productName = "Netly",
        category = ProductCategory.NETWORKING,
        tagline = "Build meaningful professional connections",
        founderName = "Harcourtian Labs",
        location = "Hyderabad"
    ),
    HProduct(
        id = "9",
        productName = "GradLink",
        category = ProductCategory.ALUMNI,
        tagline = "From campus to career success",
        founderName = "EduTech Collective",
        location = "Chennai"
    ),
    HProduct(
        id = "10",
        productName = "CareerBond",
        category = ProductCategory.CAREER,
        tagline = "Where careers connect",
        founderName = "Harcourtian Team",
        location = "Remote"
    )
)







