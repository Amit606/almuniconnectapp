// HomeScreen.kt
// Jetpack Compose Home screen for "AlumniConnect" app
// Features: top app bar with search & notifications, banner, horizontal lists (Events, Jobs), feed (alumni posts), FAB, and bottom navigation.

package com.kwh.almuniconnect.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.almunipost.AlumniStory
import com.kwh.almuniconnect.almunipost.alumniFeed
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
    val context = LocalContext.current

    val bottomBarState = remember { mutableStateOf(BottomNavItem.Home) }
    val userPrefs = remember { UserPreferences(context) }
//    val user by userPrefs.getUser().collectAsState(
//        initial = UserLocalModel()
//    )
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
                        style = MaterialTheme.typography.labelLarge,
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

                    IconButton(onClick = {
                        navController.navigate(Routes.USER_PROFILE)
                    }) {
//                        if (user.photo.isNotEmpty()) {
//                            AsyncImage(
//                                model = user.photo,
//                                contentDescription = "Profile photo",
//                                modifier = Modifier
//                                    .size(36.dp)
//                                    .clip(CircleShape),
//                                contentScale = ContentScale.Crop
//                            )
//                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.girl),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                       // }
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
                // Banner / Welcome card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = R.drawable.hbtu,
                            contentDescription = "Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(16.dp)
                        ) {
                        }
                    }
                }
            }

            // Events section
            item {
                SectionTitle(
                    title = "Upcoming Events",
                    actionText = "View All",
                    onAction = {
                        navController.navigate(Routes.EVENTS)
                    }
                )
            }

            item {
                val sampleEvents = sampleEvents()
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleEvents) { event ->
                        EventCard(event = event, onClick = {   navController.navigate(
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
                        navController.navigate(Routes.EVENTS)
                    }
                )
            }

            item {
                val sampleEvents = getDummyProducts()
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleEvents) { event ->
                        ProductCard(event = event, onClick = {  navController.navigate(Routes.EVENTS) })
                    }
                }
            }

            // Jobs section
            item {
                SectionTitle(title = "Jobs & Opportunities", actionText = "More", onAction = {
                    navController.navigate(Routes.JOB_DETAILS)
                })
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(dummyJobPosts) { job ->
                        JobCard(job = job, onClick = {
                            navController.navigate("job_details/${job.jobId}")
                        }
                        )
                    }
                }
            }

            // Feed
            item {
                SectionTitle(title = "Alumni Stories & Achievements", actionText = "View All", onAction = {
                    navController.navigate(Routes.ALMUNI_POST)
                })
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(alumniFeed) { post ->
                        AlumniPost(post = post, onClick={
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
            }


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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF4F1)
        ),
        shape = RoundedCornerShape(14.dp),
       // Soft premium border
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(event.title, style = MaterialTheme.typography.titleMedium, color =Color.Black,fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Text(event.date, color =Color.Black,style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(event.location,color =Color.Black,style = MaterialTheme.typography.bodySmall)
        }
    }
}

// Job card
@Composable
fun JobCard(job: JobAPost, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(120.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF4F1)
        ),
        shape = RoundedCornerShape(14.dp),

        elevation = CardDefaults.cardElevation(8.dp),
    ){
        Column(modifier = Modifier.padding(12.dp)) {
            Text(job.title,color=Color.Black, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(job.description,color=Color.Black, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.weight(1f))
            Text(job.location,color=Color.Black, style = MaterialTheme.typography.bodySmall)
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
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF4F1)
        ),
        shape = RoundedCornerShape(14.dp),

        elevation = CardDefaults.cardElevation(8.dp),
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
            label = { Text("Network") }
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
        productName = "Harcourtian Connect",
        category = ProductCategory.ALUMNI,
        tagline = "Connecting alumni across generations",
        founderName = "Harcourtian Labs",
        location = "Kanpur"
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
fun AlumniNews(post: UniversityNews,
               onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .height(120.dp)
            .clickable{onClick()},
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF4F1)
        ),

        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = post.title,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = post.description,
                color = Color.DarkGray,

                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = post.date,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
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
            .padding(horizontal = 6.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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