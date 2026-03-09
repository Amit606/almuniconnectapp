package com.kwh.almuniconnect.jobposting
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar



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
        }
    ) { paddingValues ->

        when (val result = state.value) {

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

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(result.message)
                }
            }

            is JobState.Success -> {

                val job = result.jobs.find { it.jobId == jobId }

                if (job == null) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Job not found")
                    }

                } else {

                    JobDetailContent(job, paddingValues)

                }
            }
        }
    }
}
@Composable
fun JobDetailContent(job: JobAPost, paddingValues: PaddingValues) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White)
            .padding(16.dp)
    ) {

        item {

            Text(
                text = job.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = job.description,
                style = MaterialTheme.typography.labelMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        item {

            InfoRow("📍 Location", job.location)
            InfoRow("💼 Experience", job.totalExperience)
            InfoRow("💰 Salary", job.salary)
            InfoRow("🕒 Job Type", job.employmentType)

        }

        item {

            SectionTitle("Job Description")

            Text(
                text = job.description,
                style = MaterialTheme.typography.labelSmall
            )
        }

        item {

            SectionTitle("Job Referred By")

            Text("Amit Kumar Gupta")
            Text("Datability Technology")
        }

        item {

            Spacer(modifier = Modifier.height(24.dp))

            ApplyButton()
        }
    }
}
@Composable
fun InfoRow(label: String, value: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SectionTitle(title: String) {

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = title,
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ApplyButton() {

    Button(
        onClick = {
            // TODO open WhatsApp / Email / Apply link
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {

        Text(
            text = "Apply Now",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}