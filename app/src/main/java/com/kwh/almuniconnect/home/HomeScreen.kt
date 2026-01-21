// HomeScreen.kt
// Jetpack Compose Home screen for "AlumniConnect" app
// Features: top app bar with search & notifications, banner, horizontal lists (Events, Jobs), feed (alumni posts), FAB, and bottom navigation.

package com.kwh.almuniconnect.home
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import android.R.attr.background
import android.R.attr.onClick
import android.R.id.background
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
        "https://www.instagram.com/p/C_LjJeYSY4E/",
        "https://yourcdn.com/images/banner3.jpg",
        "https://yourcdn.com/images/banner4.jpg",
        "https://yourcdn.com/images/banner5.jpg"
    )

    val context = LocalContext.current
    TrackScreen("home_screen")

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
                    IconButton(onClick = { /* open notifications */ }) {
                        Icon(
                            Icons.Default.Notifications,
                            tint = Color(0xFFF5B700), // Gold highlight
                            contentDescription = "Notifications"
                        )
                    }
                    IconButton(onClick = { navController.navigate(Routes.SUBSCRIPTION) }) {
                        Icon(
                            Icons.Default.WorkspacePremium,
                            tint = Color(0xFFF5B700), // Gold highlight
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
                        contentPadding = PaddingValues(horizontal = 16.dp),
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
                        navController.navigate(Routes.SERVICE_DETAILS)
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
@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)

       // Soft premium border
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Text(event.date,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4A4F5A)
            )
            Spacer(modifier = Modifier.weight(1f))
            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF7A8194),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A8194)
                )
            }
        }
    }
}


@Composable
fun JobMiniCard(job: JobAPost, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick),
           // .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Top Row: Logo + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.playstore),
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Text(
                    text = "3d ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A8194)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Job Title
            Text(
                text = job.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Company + Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = job.employmentType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4A4F5A)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = job.totalExperience,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4A4F5A)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF7A8194),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = job.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A8194)
                )
            }
        }
    }
}


// Alumni post
@Composable
fun AlumniPost(post: AlumniStory,
     onClick: () -> Unit

) {
    Card(
        modifier = Modifier.width(250.dp)
            .height(120.dp)
            .clickable{onClick()},
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            post.imageRes?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.girl),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(post.name, color = Color.Black,  style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(post.title,color=Color.Black, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(6.dp))
                Text(post.companyOrStartup, color = Color.Black, style = MaterialTheme.typography.bodySmall,)


            }
        }
    }
}

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
    Event("2", "Tech Talk: AI in Industry", "Feb 05, 2026", "Online"),
    Event("3", "Regional Chapter: Delhi", "Mar 12, 2026", "Delhi")
)




private fun sampleNews(): List<UniversityNews> {
    return listOf(
        UniversityNews(
            id = "1",
            title = "HBTU MCA Alumni Meet 2026 Announced 🎉",
            description = "The grand HBTU MCA Alumni Meet 2026 will be held on 22 February 2026. Registrations are open now.",
            date = "05 Jan 2026"
        ),
        UniversityNews(
            id = "2",
            title = "New MCA Lab Inaugurated",
            description = "A new advanced computer lab has been inaugurated for MCA students with modern infrastructure.",
            date = "28 Dec 2025"
        ),
        UniversityNews(
            id = "3",
            title = "HBTU Ranked Among Top Technical Universities",
            description = "HBTU has been ranked among the top engineering institutions in India for academic excellence.",
            date = "15 Dec 2025"
        ),
        UniversityNews(
            id = "4",
            title = "MCA Placement Drive 2026 Begins",
            description = "The MCA placement drive for the 2026 batch has started with multiple reputed IT companies participating.",
            date = "10 Dec 2025"
        ),
        UniversityNews(
            id = "5",
            title = "HBTU Hosts National Tech Fest",
            description = "HBTU successfully hosted a national-level technical fest with participation from colleges across India.",
            date = "02 Dec 2025"
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

@Composable
fun AlumniNews(
    post: UniversityNews,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Title
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C1C1E),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF5A5A5A),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color(0xFFE6E9F0))

            Spacer(modifier = Modifier.height(8.dp))

            // Date Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    tint = Color(0xFF8E8E93),
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = post.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8E8E93)
                )
            }
        }
    }
}


@Composable
fun ProductCard(
    event: HProduct,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
           // .padding(horizontal = 6.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // 🔖 Category Badge
            Box(
                modifier = Modifier
                    .background(
                        color = categoryColor(event.category),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = event.category.name,
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🏷 Product Name
            Text(
                text = event.productName,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 📝 Tagline
            Text(
                text = event.tagline,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            // 👤 Founder & Location
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "By ${event.founderName}",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = event.location,
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}
@Composable
fun categoryColor(category: ProductCategory): Color {
    return when (category) {
        ProductCategory.ALUMNI -> Color(0xFF6C63FF)
        ProductCategory.CAREER -> Color(0xFF4CAF50)
        ProductCategory.EDUCATION -> Color(0xFF2196F3)
        ProductCategory.NETWORKING -> Color(0xFFFF9800)
    }
}