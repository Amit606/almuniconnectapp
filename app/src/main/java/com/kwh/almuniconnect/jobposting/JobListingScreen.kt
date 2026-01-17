package com.kwh.almuniconnect.jobposting

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.compose.runtime.getValue
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.utils.CommonEmptyState
import com.kwh.almuniconnect.utils.encodeRoute

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
                        JobCard(job, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun JobCard(job:JobAPost ,navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),

        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = job.title,
                style = MaterialTheme.typography.titleMedium

            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = job.description,  style = MaterialTheme.typography.labelMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📍 ${job.location}", style = MaterialTheme.typography.labelSmall)
                Text("💼 ${job.totalExperience}", style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text("💰 ${job.salary}",  style = MaterialTheme.typography.labelSmall)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    Log.e("JobCard", "Navigating to job details for ID: ${job.jobId}")
                    val encodedJobId = Uri.encode(job.jobId)
                    Log.e("encodedJobId", "Navigating to job details for ID: ${encodedJobId}")

                    navController.navigate("job_details/${encodedJobId}"
                    ) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Show Full Details", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

