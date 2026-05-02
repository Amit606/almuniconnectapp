package com.kwh.almuniconnect.jobposting.jobprofile
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobProfileScreen(onSave: (JobProfile) -> Unit) {

    var name by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var skillInput by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf(listOf<String>()) }
    var openToWork by remember { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        imageUri = it
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Job Profile") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Profile Image
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                IconButton(onClick = { launcher.launch("image/*") }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                }
            }

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Job Title") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = experience,
                onValueChange = { experience = it },
                label = { Text("Experience (Years)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Skills Input
            Row {
                OutlinedTextField(
                    value = skillInput,
                    onValueChange = { skillInput = it },
                    label = { Text("Add Skill") },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    if (skillInput.isNotEmpty()) {
                        skills = skills + skillInput
                        skillInput = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }

            LazyRow {
                items(skills) { skill ->
                    AssistChip(
                        onClick = {},
                        label = { Text(skill) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            // Open to Work
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Open to Work")
                Switch(checked = openToWork, onCheckedChange = { openToWork = it })
            }

            Button(
                onClick = {
                    onSave(
                        JobProfile(
                            name, title, company, experience, skills, openToWork, imageUri
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Profile")
            }
        }
    }
}
@Composable
fun JobProfileCard(profile: JobProfile) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = rememberAsyncImagePainter(profile.imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(profile.name, style = MaterialTheme.typography.titleMedium)
                    Text("${profile.title} @ ${profile.company}")
                    Text("${profile.experience} years experience")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow {
                items(profile.skills) {
                    AssistChip(
                        onClick = {},
                        label = { Text(it) },
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (profile.openToWork) {
                Text(
                    "Open to Work",
                    color = Color.Green,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}