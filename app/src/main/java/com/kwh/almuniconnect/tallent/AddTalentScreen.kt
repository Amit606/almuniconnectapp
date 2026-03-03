package com.kwh.almuniconnect.tallent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddTalentScreen(
    navController: NavController
) {

    val talentOptions = listOf(
        "🎤 Singer",
        "🎼 Musician",
        "💃 Dancer",
        "📚 Educator",
        "✍️ Poetry",
        "🖊 Writer",
        "🎙 Public Speaker",
        "💻 Developer",
        "🎨 Designer",
        "🚀 Entrepreneur",
        "🏏 Sports",
        "✨ Other"
    )
    var selectedTalent by remember { mutableStateOf("") }

    val viewModel: TalentViewModel = viewModel()
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var talent by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(initial = UserLocalModel())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Talent") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {


            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Select Talent",
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                talentOptions.forEach { option ->

                    val isSelected = selectedTalent == option

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTalent = option },
                        label = {
                            Text(
                                text = option,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else
                                MaterialTheme.colorScheme.surface,
                            labelColor = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = link,
                onValueChange = { link = it },
                label = { Text("YouTube or Other Showcase Link") },
                leadingIcon = {
                    Icon(Icons.Default.Link, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Add your talent description") },
                leadingIcon = {
                    Icon(Icons.Default.Description, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    isLoading = true
                    viewModel.addTalent(
                        name = user.name?: "Anonymous",
                        branch = user.branch ?: "",
                        year = user.year?.toIntOrNull() ?: 0,
                        photo = user.photo ?: "",
                        skill = selectedTalent,
                        videoLink = link,
                        description=description,
                        userId = user.userId?: "anonymous",
                        email = user.email ?: ""
                    )

                    isLoading = false
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedTalent.isNotBlank() &&
                        link.isNotBlank() && description.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Submit Talent")
                }
            }
        }
    }
}