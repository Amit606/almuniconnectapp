package com.kwh.almuniconnect.jobposting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostScreen(
    navController: NavController,
    onSubmit: (JobPost) -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var jobType by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var applyEmail by remember { mutableStateOf("") }
    var websiteUrl by remember { mutableStateOf("") }
    var linkedinUrl by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Job Post",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()                       // 🔥 IMPORTANT
              //  .background(Color(0xFF0E1420))
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            SectionHeader("Job Information")
            AppTextField("Job Title", title) { title = it }
            AppTextField("Company Name", company) { company = it }
            AppTextField("Location", location) { location = it }
            AppTextField("Experience (e.g. 2–5 Years)", experience) { experience = it }
            AppTextField("Salary (e.g. ₹10–15 LPA)", salary) { salary = it }
            AppTextField("Job Type (Full-time / Remote)", jobType) { jobType = it }

            SectionHeader("Skills & Description")

            AppTextField(
                label = "Skills (comma separated)",
                value = skills,
                onValueChange = { skills = it }
            )

            AppTextField(
                label = "Job Description",
                value = description,
                onValueChange = { description = it },
            )

            SectionHeader("Apply Details")

            AppTextField("Apply Email", applyEmail) { applyEmail = it }
            AppTextField("Company Website URL", websiteUrl) { websiteUrl = it }
            AppTextField("LinkedIn Job URL", linkedinUrl) { linkedinUrl = it }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
//                    onSubmit(
//                        JobPost(
//                            title,
//                            company,
//                            location,
//                            experience,
//                            salary,
//                            jobType,
//                            skills,
//                            description,
//                            applyEmail,
//                            websiteUrl,
//                            linkedinUrl
//                        )
//                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publish Job")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = text,
        color=Color.Black,
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(8.dp))
}
@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label,color=Color.Black,  style = MaterialTheme.typography.titleMedium) },   // ✅ correct
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        maxLines = 1,
        textStyle = TextStyle(color=Color.Black),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6A5AE0),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.White
        )
    )
}

