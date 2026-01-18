package com.kwh.almuniconnect.jobposting
import android.os.Bundle
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
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun JobDetailScreen(navController: NavController,job: JobAPost) {

    TrackScreen("job_detail_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Job Details",
                navController = navController
            )
        }
    ) { paddingValues ->
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

                Text(job.description,  style = MaterialTheme.typography.labelMedium)

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
                Text(job.description,  style = MaterialTheme.typography.labelSmall,
                )
            }
            item {
                SectionTitle("Job Referer by")
                Text("Amit Kumar Gupta",  style = MaterialTheme.typography.labelSmall,)
                Text("Datability Technology",  style = MaterialTheme.typography.labelSmall,)

            }



            item {
                Spacer(modifier = Modifier.height(24.dp))
                ApplyButton()
            }
        }
    }
}
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value,  style = MaterialTheme.typography.bodySmall)
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
            // TODO: open email / web / WhatsApp
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("Apply Now", style = MaterialTheme.typography.bodyMedium)
    }
}
