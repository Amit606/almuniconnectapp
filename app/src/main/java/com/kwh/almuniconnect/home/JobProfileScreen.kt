package com.kwh.almuniconnect.home

import android.net.Uri
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar

// ================= DATA MODELS =================
data class JobProfileData(
    val fullName: String,
    val email: String,
    val phone: String,
    val location: String,
    val summary: String,
    val skills: List<String>,
    val experienceLevel: String, // "Fresher" or "Experienced"
    val yearsOfExp: String,
    val jobStatus: String,      // "Actively Looking", "Immediate Joiner", etc.
    val education: String,
    val resumeUri: Uri?,
    val resumeFileName: String?
)

// ================= VALIDATION =================
fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
fun isValidPhone(phone: String) = phone.length in 10..13 && phone.all { it.isDigit() }

// ================= MAIN SCREEN =================
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JobProfileScreen(
    navController: NavController,
    onSaveProfile: (JobProfileData) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    // Basic Info State
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }

    // Job Specific State
    var experienceLevel by remember { mutableStateOf("Fresher") }
    var yearsOfExp by remember { mutableStateOf("") }
    var jobStatus by remember { mutableStateOf("Actively Looking") }
    var skills by remember { mutableStateOf(listOf<String>()) }
    var skillInput by remember { mutableStateOf("") }

    // Resume State
    var resumeUri by remember { mutableStateOf<Uri?>(null) }
    var resumeName by remember { mutableStateOf<String?>(null) }

    // UI State
    var expandedStatus by remember { mutableStateOf(false) }
    val statusOptions = listOf("Actively Looking", "Immediate Joiner", "Open to Offers", "Not Looking")

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        resumeUri = uri
        resumeName = uri?.lastPathSegment?.substringAfterLast("/")
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Create Job Profile",
                showBack = true,
                navController = navController
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp, modifier = Modifier.navigationBarsPadding()) {
                Button(
                    onClick = {
                        onSaveProfile(
                            JobProfileData(
                                fullName, email, phone, location, summary, skills,
                                experienceLevel, yearsOfExp, jobStatus, education, resumeUri, resumeName
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    enabled = fullName.isNotBlank() && email.isNotBlank() && resumeUri != null
                ) {
                    Icon(Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save & Apply")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // --- SECTION: EXPERIENCE TYPE ---
            item {
                Text("Employment Status", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = experienceLevel == "Fresher",
                        onClick = { experienceLevel = "Fresher"; yearsOfExp = "0" },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                    ) { Text("Fresher") }
                    SegmentedButton(
                        selected = experienceLevel == "Experienced",
                        onClick = { experienceLevel = "Experienced" },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                    ) { Text("Experienced") }
                }
            }

            // --- SECTION: PERSONAL INFO ---
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Personal Details", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (experienceLevel == "Experienced") {
                        OutlinedTextField(
                            value = yearsOfExp,
                            onValueChange = { if (it.all { c -> c.isDigit() }) yearsOfExp = it },
                            label = { Text("Years of Experience") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // --- SECTION: JOB SEARCH STATUS ---
            item {
                ExposedDropdownMenuBox(
                    expanded = expandedStatus,
                    onExpandedChange = { expandedStatus = !expandedStatus }
                ) {
                    OutlinedTextField(
                        value = jobStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Availability Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedStatus,
                        onDismissRequest = { expandedStatus = false }
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = { jobStatus = option; expandedStatus = false }
                            )
                        }
                    }
                }
            }

            // --- SECTION: EDUCATION ---
            item {
                OutlinedTextField(
                    value = education,
                    onValueChange = { education = it },
                    label = { Text("Highest Education (e.g. B.Tech Computer Science)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // --- SECTION: SKILLS ---
            item {
                Text("Skills", style = MaterialTheme.typography.titleMedium)
                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    skills.forEach { skill ->
                        InputChip(
                            selected = true,
                            onClick = { skills = skills - skill },
                            label = { Text(skill) },
                            trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(16.dp)) }
                        )
                    }
                }
                OutlinedTextField(
                    value = skillInput,
                    onValueChange = { skillInput = it },
                    label = { Text("Add Skill") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            if (skillInput.isNotBlank()) {
                                skills = skills + skillInput.trim()
                                skillInput = ""
                            }
                        }) { Icon(Icons.Default.Add, null) }
                    }
                )
            }

            // --- SECTION: RESUME ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CloudUpload, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = if (resumeName == null) "Upload Resume (PDF)" else "Selected: $resumeName",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (resumeName != null) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { picker.launch("application/pdf") }) {
                            Text(if (resumeName == null) "Choose File" else "Change File")
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}