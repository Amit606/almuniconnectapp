package com.kwh.almuniconnect.jobposting

import PremiumDialog
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.network.openUrl
import com.kwh.almuniconnect.utils.CommonEmptyState
import com.kwh.almuniconnect.utils.getTimeAgo

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListingScreen(navController: NavController) {

    // 🔹 Create API & ViewModel (correct)
    val apiService = remember {
        NetworkClient.createService(ApiService::class.java)
    }
    val repository = remember { JobRepository(apiService) }
    val viewModel: JobViewModel = viewModel(
        factory = JobViewModelFactory(repository)
    )
    TrackScreen("job_listing_screen")
    var showSubscriptionDialog by remember { mutableStateOf(false) }

    // 🔹 Collect state from ViewModel
    val state by viewModel.state.collectAsState()

    // 🔹 Load jobs once
    LaunchedEffect(Unit) {
        viewModel.loadJobs()
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Job Listings",
                navController = navController,

                rightAction = {
                    IconButton(
                        onClick = {
                            if (true) {
                                navController.navigate(Routes.JOB_POST)
                            } else {
                                showSubscriptionDialog = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PostAdd,
                            contentDescription = "Add Job"
                        )
                    }

                    }

            )
        }
    ) { paddingValues ->

        when (state) {

            is JobState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is JobState.Error -> {
                CommonEmptyState(
                    title = "No Upcoming Jobs",
                    message = "There are no upcoming jobs right now.\nPlease check back later.",
                    lottieRes = R.raw.no_events,
                    actionText = "Refresh",
                    onActionClick = { viewModel.loadJobs() }
                )
            }

            is JobState.Success -> {
                val jobList = (state as JobState.Success).jobs

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentPadding = paddingValues
                ) {
                    items(jobList) { job ->
                        JobCard(job,navController)
                    }
                }
            }
        }
        if (showSubscriptionDialog) {
            PremiumDialog(
                onConnectNow = {
                    showSubscriptionDialog = false
                    navController.navigate(Routes.SUBSCRIPTION)
                },
                onCancel = {
                    showSubscriptionDialog = false
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobCard(job: JobAPost, navController: NavController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // Title
            Text(
                text = "${job.title} | ${job.totalExperience ?: "Not specified"} | ${job.location ?: "Remote"}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Experience, Salary, Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                InfoItem(
                    Icons.Default.Work,
                    job.totalExperience ?: "Not specified"
                )

                InfoItem(
                    Icons.Default.CurrencyRupee,
                    job.salary ?: "Not disclosed"
                )

                InfoItem(
                    Icons.Default.LocationOn,
                    job.location ?: "Remote"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            InfoItem(
                Icons.Default.Timer,
                job.employmentType ?: "Full Time"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = job.description,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = getTimeAgo(job.createdAtUtc),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Button(
                    onClick = {
                        navController.navigate("job_details/${job.jobId}")
                    },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 8.dp
                    )
                ) {
                    Text(
                        text = "Explore Job",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }
            Row(verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(job.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Alumni Photo",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(R.drawable.man),
                    error = painterResource(R.drawable.man),
                    fallback = painterResource(R.drawable.man)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Posted by ${job.alumniName}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}
@Composable
fun InfoItemClick(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SkillChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFF2F4F7)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SubscriptionRequiredDialog(
    onDismiss: () -> Unit,
    onSubscribe: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Premium Feature 🔒")
        },
        text = {
            Text(
                "Posting a job is a premium feature.\n\n" +
                        "Please take a subscription to continue and reach more alumni."
            )
        },
        confirmButton = {
            TextButton(onClick = onSubscribe) {
                Text("View Plans")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not Now")
            }
        }
    )
}
