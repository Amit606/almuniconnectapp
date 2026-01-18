package com.kwh.almuniconnect.jobposting

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.utils.CommonEmptyState
import com.kwh.almuniconnect.utils.encodeRoute
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
                navController = navController
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
                    title = "No Upcoming Events",
                    message = "There are no upcoming events right now.\nPlease check back later.",
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
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JobCard(job:JobAPost,navController: NavController) {
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
                text = "${job.title} | ${job.totalExperience} | ${job.location}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Company + Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Capgemini",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "3.7 | 51573 Reviews",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Experience, Salary, Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(Icons.Default.Work, job.totalExperience)
                InfoItem(Icons.Default.CurrencyRupee, "Not disclosed")
                InfoItem(Icons.Default.LocationOn, job.location)

            }
            Spacer(modifier = Modifier.height(10.dp))
            InfoItem(Icons.Default.Timer, job.employmentType)

            Spacer(modifier = Modifier.height(10.dp))

            // Skills
            Text(
                text = job.description,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoItem(Icons.Default.Person, " Job Posted By Amit Kumar Gupta")

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
                    onClick = { navController.navigate(Routes.EVENTS) },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Apply Now",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
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

