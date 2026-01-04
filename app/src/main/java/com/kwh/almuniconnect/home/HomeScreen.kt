// HomeScreen.kt
// Jetpack Compose Home screen for "AlumniConnect" app
// Features: top app bar with search & notifications, banner, horizontal lists (Events, Jobs), feed (alumni posts), FAB, and bottom navigation.

package com.kwh.almuniconnect.home

import android.R.attr.background
import android.R.id.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.kwh.almuniconnect.network.NetworkScreen
import com.kwh.almuniconnect.permission.RequestNotificationPermission
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,

    onOpenProfile: () -> Unit = {},
    onOpenMessages: () -> Unit = {},
    onOpenEventDetails: (Event) -> Unit = {},
    onOpenJobDetails: (Job) -> Unit = {},
    onCreatePost: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val bottomBarState = remember { mutableStateOf(BottomNavItem.Home) }
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(
        initial = UserLocalModel("", "", "", "")
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
                title = { Text("AlumniConnect", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { /* open drawer */ }) {
                        Icon(Icons.Default.Menu, tint = Color.White, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { /* open notifications */ }) {
                        Icon(Icons.Default.Notifications, tint = Color.White,contentDescription = "Notifications")
                    }
                    IconButton(onClick = onOpenProfile) {

                        if (user.photo.isNotEmpty()) {
                            // 🔹 Google profile photo
                            AsyncImage(
                                model = user.photo,
                                contentDescription = "Profile photo",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // 🔹 Fallback local avatar
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
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420),
                    titleContentColor = Color.White
                )
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
                    BottomNavItem.Messages -> onOpenMessages()
                    BottomNavItem.Profile -> onOpenProfile()
                }
            }
        }

    ) { paddingValues ->
        // Content
        LazyColumn(
            modifier = Modifier
                .background(Color(0xFF0d1024))
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
                        .height(140.dp)
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
                            Text("Welcome back!"+user.name, fontSize = 18.sp, color = Color.Green)
                            Text("See what's happening in your alumni network", maxLines = 2,color = Color.LightGray, overflow = TextOverflow.Ellipsis)
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
                        EventCard(event = event, onClick = {  navController.navigate(Routes.EVENTS) })
                    }
                }
            }

            // Jobs section
            item {
                SectionTitle(title = "Jobs & Opportunities", actionText = "More", onAction = {
                    navController.navigate(Routes.EVENTS)
                })
            }

            item {
                val sampleJobs = sampleJobs()
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sampleJobs) { job ->
                        JobCard(job = job, onClick = { navController.navigate(job) })
                    }
                }
            }

            // Feed
            item {
                SectionTitle(title = "Alumni Feed", actionText = "New Post", onAction = {
                    navController.navigate(Routes.EVENTS)
                })
            }

            items(samplePosts()) { post ->
                AlumniPost(post = post)
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
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
        Text(title, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.SemiBold)
        Text(actionText,color = Color.White, modifier = Modifier.clickable(onClick = onAction))
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

        shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                containerColor = Color(0xFF142338)
                )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(event.title,color = Color.White, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Text(event.date,color = Color.White, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.weight(1f))
            Text(event.location,color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// Job card
@Composable
fun JobCard(job: Job, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF142338)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(job.title,color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(job.company,color = Color.White, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.weight(1f))
            Text(job.location, color = Color.White,style = MaterialTheme.typography.bodySmall)
        }
    }
}

// Alumni post
@Composable
fun AlumniPost(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF142338)
        )
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Image(
                painter = painterResource(id = R.drawable.girl),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(post.name,  color = Color.White, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(post.timeAgo, color = Color.LightGray,style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(post.content,color = Color.White)
                post.imageUrl?.let { url ->
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(model = url, contentDescription = null, modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.Crop)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = { /* like */ }) { Icon(Icons.Default.FavoriteBorder, tint = Color.White, contentDescription = "Like") }
                    IconButton(onClick = { /* comment */ }) { Icon(Icons.Default.ChatBubbleOutline,tint = Color.White, contentDescription = "Comment") }
                    IconButton(onClick = { /* share */ }) { Icon(Icons.Default.Share,tint = Color.White, contentDescription = "Share") }
                }
            }
        }
    }
}

// Bottom navigation items
enum class BottomNavItem { Home, Network, Messages, Profile }

@Composable
fun BottomAppBarWithNav(
    selected: BottomNavItem,
    onSelect: (BottomNavItem) -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFF142338),
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
            selected = selected == BottomNavItem.Messages,
            onClick = { onSelect(BottomNavItem.Messages) },
            icon = { Icon(Icons.Default.Message, contentDescription = null) },
            label = { Text("Messages") }
        )

        NavigationBarItem(
            selected = selected == BottomNavItem.Profile,
            onClick = { onSelect(BottomNavItem.Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") }
        )
    }}
}

// Sample models + data
data class Event(val id: String, val title: String, val date: String, val location: String)
data class Job(val id: String, val title: String, val company: String, val location: String)
data class Post(val id: String, val name: String, val timeAgo: String, val content: String, val imageUrl: String?)

private fun sampleEvents() = listOf(
    Event("1", "MCA Almuni Meet 2026", "Feb 22, 2026", "Delhi/NCR"),
    Event("2", "Tech Talk: AI in Industry", "Feb 05, 2026", "Online"),
    Event("3", "Regional Chapter: Delhi", "Mar 12, 2026", "Delhi")
)

private fun sampleJobs() = listOf(
    Job("1", "Senior Android Engineer", "Acme Corp", "Remote"),
    Job("2", "Product Manager", "Beta Inc.", "Bengaluru"),
    Job("3", "UI/UX Designer", "DesignCo", "Hyderabad")
)

private fun samplePosts() = listOf(
    Post("1", "Amit Kumar Gupta ", "2h", "Excited to announce our alumni meetup next month!", null),
    Post("2", "Anita Desai", "1d", "We are hiring for multiple roles at our startup.", null)
)


