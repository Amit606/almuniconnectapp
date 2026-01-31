package com.kwh.almuniconnect.jobposting
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.api.JobPostRequest
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostScreen(
    navController: NavController,
    viewModel: JobPostViewModel = viewModel(),
    onSubmit: (JobPost) -> Unit = {}
) {
    val jobTypes = listOf("Full-time", "Part-time", "Remote", "Hybrid", "Internship", "Contract")

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

    var showErrors by remember { mutableStateOf(false) }

    val isFormValid =
        title.isNotBlank() &&
                company.isNotBlank() &&
                location.isNotBlank() &&
                experience.isNotBlank() &&
                jobType.isNotBlank() &&
                skills.isNotBlank() &&
                description.isNotBlank() &&
                isValidEmail(applyEmail) &&
                isValidUrl(websiteUrl) &&
                isValidUrl(linkedinUrl)

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Post a Job",
                navController = navController
            )
        },
        contentColor = Color.White
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            SectionHeader("Job Information")

            AppTextField(
                "Job Title",
                title,
                { title = it },
                isError = showErrors && title.isBlank(),
                errorText = "Job title required"
            )

            AppTextField(
                "Company Name",
                company,
                { company = it },
                isError = showErrors && company.isBlank(),
                errorText = "Company name required"
            )

            AppTextField(
                "Location",
                location,
                { location = it },
                isError = showErrors && location.isBlank(),
                errorText = "Location required"
            )

            AppTextField(
                "Experience (e.g. 2–5 Years)",
                experience,
                { experience = it },
                isError = showErrors && experience.isBlank(),
                errorText = "Experience required"
            )

            AppTextField("Salary (Optional)", salary, { salary = it })

            AppTextField(
                "Job Type (Full-time / Remote)",
                jobType,
                { jobType = it },
                isError = showErrors && jobType.isBlank(),
                errorText = "Job type required"
            )

            SectionHeader("Skills & Description")

            AppTextField(
                "Skills (comma separated)",
                skills,
                { skills = it },
                isError = showErrors && skills.isBlank(),
                errorText = "Skills required"
            )

            AppTextField(
                "Job Description",
                description,
                { description = it },
                isError = showErrors && description.isBlank(),
                errorText = "Description required",
                maxLines = 4
            )

            SectionHeader("Apply Details")

            AppTextField(
                "Apply Email",
                applyEmail,
                { applyEmail = it },
                keyboardType = KeyboardType.Email,
                isError = showErrors && !isValidEmail(applyEmail),
                errorText = "Valid email required"
            )

            AppTextField(
                "Company Website URL",
                websiteUrl,
                { websiteUrl = it },
                keyboardType = KeyboardType.Uri,
                isError = showErrors && !isValidUrl(websiteUrl),
                errorText = "Invalid website URL"
            )

            AppTextField(
                "LinkedIn Job URL",
                linkedinUrl,
                { linkedinUrl = it },
                keyboardType = KeyboardType.Uri,
                isError = showErrors && !isValidUrl(linkedinUrl),
                errorText = "Invalid LinkedIn URL"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    showErrors = true
                    if (isFormValid) {
                        viewModel.postJob(
                            JobPostRequest(
                                title = title,
                                description = description,
                                company = company,
                                location = location,
                                employmentType = jobType,
                                totalExperience = experience,
                                salary = salary,
                                expiresAt = "2026-01-31" // TODO: DatePicker later
                            )
                        )
                    }
                },
                enabled = isFormValid && !viewModel.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Publish Job", fontSize = 16.sp)
                }
            }
            LaunchedEffect(viewModel.success) {
                if (viewModel.success) {
                    Toast.makeText(
                        navController.context,
                        "Job posted successfully!",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                }
            }

            viewModel.error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
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
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onDone: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        maxLines = 1,
        textStyle = TextStyle(color = Color.Black),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone?.invoke()
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF6A5AE0),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.White
        )
    )
}

@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    errorText: String = "",
    maxLines: Int = 1
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            )
        )

        if (isError) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun isValidUrl(url: String): Boolean {
    if (url.isBlank()) return true
    return android.util.Patterns.WEB_URL.matcher(url).matches()
}


