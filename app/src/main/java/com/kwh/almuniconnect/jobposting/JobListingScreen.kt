package com.kwh.almuniconnect.jobposting

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.home.Job
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.IconButton
import com.kwh.almuniconnect.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListingScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Job Openings") },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.JOB_POST)
                            // TODO: Navigate to JobPostScreen
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = "Post Job"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(jobList) { job ->
                JobCard(job,navController)
            }
        }
    }
}
@Composable
fun JobCard(job: Job,navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = job.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = job.company, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📍 ${job.location}")
                Text("💼 ${job.experience}")
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text("💰 ${job.salary}", fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate(Routes.JOB_DETAILS_Full) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Show Full Details")
            }
        }
    }
}
val jobList = listOf(
    Job("1", "Android Developer", "Google", "Bangalore", "2–5 Years", "₹15–25 LPA"),
    Job("2", "Backend Developer", "Amazon", "Hyderabad", "3–6 Years", "₹18–30 LPA"),
    Job("3", "UI/UX Designer", "Flipkart", "Remote", "1–3 Years", "₹8–15 LPA"),
    Job("4", "Flutter Developer", "Startup", "Noida", "1–4 Years", "₹6–12 LPA")
)
