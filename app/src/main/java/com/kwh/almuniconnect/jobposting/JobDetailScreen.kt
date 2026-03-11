package com.kwh.almuniconnect.jobposting

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.analytics.TrackScreen
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    navController: NavController,
    jobId: String
) {

    TrackScreen("job_detail_screen")

    val apiService = remember {
        NetworkClient.createService(ApiService::class.java)
    }

    val repository = remember { JobRepository(apiService) }

    val viewModel: JobViewModel = viewModel(
        factory = JobViewModelFactory(repository)
    )

    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadJobs()
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Job Details",
                navController = navController
            )
        },
        bottomBar = {

            val result = state.value
            if (result is JobState.Success) {

                val job = result.jobs.find { it.jobId == jobId }

                job?.let {
                    ApplyButton(it)
                }
            }
        }
    ) { padding ->


            when (val result = state.value) {

                is JobState.Loading -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is JobState.Error -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(result.message)
                    }
                }

                is JobState.Success -> {

                    val job = result.jobs.find { it.jobId == jobId }

                    if (job != null) {

                        JobDetailContent(job, padding)

                    } else {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Job not found")
                        }
                    }
                }
            }

    }
}

@Composable
fun JobDetailContent(job: JobAPost, padding: PaddingValues) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Color.White)
            .padding(16.dp)
    ) {

        item {

            Text(
                text = job.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            JobInfoRow(job)

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {

            SectionTitle("Job Description")

            Text(
                text = job.description,
                style = MaterialTheme.typography.bodyMedium
            )

        }
        item {

            SectionTitle("Skills Required")

            Text(
                text = job.goodToHaveSkills?:"Not Specified",
                style = MaterialTheme.typography.bodyMedium
            )

        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            SectionTitle("Job Posted By :")
            AlumniSection(job)
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun JobInfoRow(job: JobAPost) {

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        InfoItem(Icons.Default.Work, job.totalExperience ?: "Not Specified")

        InfoItem(Icons.Default.LocationOn, job.location ?: "Remote")

        InfoItem(Icons.Default.CurrencyRupee, job.salary ?: "Not Disclosed")
        InfoItem(Icons.Default.Business, job.employmentType ?: "Full Time")
        InfoItem(Icons.Default.Email, job.referenceEmail ?: "")
        InfoItem(Icons.Default.Link, job.applyNowLink ?: "")


    }
}

@Composable
fun AlumniSection(job: JobAPost) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(job.photoUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Alumni Photo",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            error = painterResource(R.drawable.man),
            placeholder = painterResource(R.drawable.man)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {

            Text(
                text = job.alumniName ?: "",
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "${job.courseName} • ${job.batch}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}



@Composable
fun SectionTitle(title: String) {

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun ApplyButton(job: JobAPost) {

    val context = LocalContext.current

    Surface(
        tonalElevation = 4.dp,
        modifier = Modifier.navigationBarsPadding()   // important
    ) {

        Button(
            onClick = {
                try {

                    val link = job.applyNowLink ?: return@Button

                    val intent = when {

                        link.contains("@") -> {
                            Intent(Intent.ACTION_SENDTO, "mailto:$link".toUri())
                        }

                        link.startsWith("http") -> {
                            Intent(Intent.ACTION_VIEW, link.toUri())
                        }

                        else -> {
                            Intent(Intent.ACTION_VIEW, "https://$link".toUri())
                        }
                    }

                    context.startActivity(intent)
                }
                catch (ex: Exception) {
                    Log.e("JobDetailScreen", "Error opening link: ${ex.localizedMessage}")
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {

            Text("Apply Now")
        }
    }
}