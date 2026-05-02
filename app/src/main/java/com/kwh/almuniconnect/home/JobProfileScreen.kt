package com.kwh.almuniconnect.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobProfileScreen(
    onBack: () -> Unit,
    onProfileSaved: () -> Unit = {}
) {
    val viewModel: JobProfileViewModel = viewModel()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val tabTitles = listOf("Basic Details", "Experience & Skills", "Resume")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complete Your Job Profile", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Tab Row
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            if (index <= pagerState.currentPage ||
                                (index == 1 && viewModel.isPage1Valid) ||
                                (index == 2 && viewModel.isPage1Valid && viewModel.isPage2Valid)) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        },
                        enabled = index == 0 ||
                                (index == 1 && viewModel.isPage1Valid) ||
                                (index == 2 && viewModel.isPage1Valid && viewModel.isPage2Valid)
                    ) {
                        Text(
                            text = title,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> BasicDetailsPage(viewModel = viewModel, onNext = {
                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                    })
                    1 -> ExperienceSkillsPage(viewModel = viewModel, onNext = {
                        coroutineScope.launch { pagerState.animateScrollToPage(2) }
                    })
                    2 -> ResumePage(onSaveAndApply = {
                        viewModel.saveAndApply()
                        onProfileSaved()
                    })
                }
            }
        }
    }
}

// ==================== PAGE 1 ====================
@Composable
private fun BasicDetailsPage(
    viewModel: JobProfileViewModel,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Personal Information", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = viewModel.fullName,
            onValueChange = viewModel::updateFullName,
            label = { Text("Full Name *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = viewModel::updateEmail,
            label = { Text("Email Address *") },
            isError = viewModel.email.isNotBlank() && !viewModel.isEmailValid,
            supportingText = {
                if (viewModel.email.isNotBlank() && !viewModel.isEmailValid) {
                    Text("Please enter a valid email", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = viewModel::updatePhone,
            label = { Text("Phone Number *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.location,
            onValueChange = viewModel::updateLocation,
            label = { Text("Current Location *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = viewModel.isPage1Valid
        ) {
            Text("Next")
        }
    }
}

// ==================== PAGE 2 ====================
@Composable
private fun ExperienceSkillsPage(
    viewModel: JobProfileViewModel,
    onNext: () -> Unit
) {
    val jobTypes = listOf(
        "Looking for Job Change",
        "Open to Work",
        "Internship",
        "Starting Something New"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Experience & Job Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = viewModel.currentJobTitle,
            onValueChange = viewModel::updateJobTitle,
            label = { Text("Current / Last Job Title *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.yearsOfExp,
            onValueChange = viewModel::updateYearsOfExp,
            label = { Text("Years of Experience *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Text("What are you looking for?", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        jobTypes.forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectJobType(type) }
            ) {
                RadioButton(
                    selected = viewModel.selectedJobType == type,
                    onClick = { viewModel.selectJobType(type) }
                )
                Text(text = type, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Key Skills *", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        OutlinedTextField(
            value = viewModel.newSkill,
            onValueChange = viewModel::updateNewSkill,
            label = { Text("Add Skill") },
            trailingIcon = {
                IconButton(onClick = viewModel::addSkill) {
                    Text("Add")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

//        FlowRow(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            viewModel.skillsList.forEach { skill ->
//                AssistChip(
//                    onClick = { },
//                    label = { Text(skill) },
//                    trailingIcon = {
//                        IconButton(onClick = { viewModel.removeSkill(skill) }) {
//                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Gray)
//                        }
//                    }
//                )
//            }
//        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = viewModel.isPage2Valid
        ) {
            Text("Next")
        }
    }
}

// ==================== PAGE 3 ====================
@Composable
private fun ResumePage(onSaveAndApply: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Upload Your Resume", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📄", fontSize = 52.sp)
                    Text("Tap to upload PDF resume")
                }
            }
        }

        OutlinedButton(
            onClick = { /* TODO: Add file picker logic */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Choose PDF File")
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onSaveAndApply,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Save Profile & Apply Now")
        }
    }
}