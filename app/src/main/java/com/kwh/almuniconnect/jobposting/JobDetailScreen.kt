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

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun JobDetailScreen(job: JobDetail,navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Job Details")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White

                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420),
                    titleContentColor = Color.White
                )
            )
        },
        contentColor = Color(0xFF0E1420)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF0E1420))
                .padding(16.dp)
        ) {

            item {
                Text(
                    text = job.title,
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(job.company, color = Color.White)

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                InfoRow("📍 Location", job.location)
                InfoRow("💼 Experience", job.experience)
                InfoRow("💰 Salary", job.salary)
                InfoRow("🕒 Job Type", job.jobType)
            }

//            item {
//                SectionTitle("Skills Required")
//                FlowRow {
//                    job.skills.forEach {
//                        SkillChip(it)
//                    }
//                }
//            }

            item {
                SectionTitle("Job Description")
                Text(job.description,color = Color.White)
            }

            item {
                SectionTitle("Responsibilities")
                job.responsibilities.forEach {
                    Text("• $it",color = Color.White)
                }
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
        Text(label,color = Color.White,
            fontWeight = FontWeight.Medium)
        Text(value, color = Color.White)
    }
    Spacer(modifier = Modifier.height(8.dp))
}
@Composable
fun SectionTitle(title: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = title,
        color = Color.White,

        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
}
@Composable
fun SkillChip(skill: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
    ) {
        Text(
            text = skill,
            color = Color.White,

            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
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
        Text("Apply Now")
    }
}
