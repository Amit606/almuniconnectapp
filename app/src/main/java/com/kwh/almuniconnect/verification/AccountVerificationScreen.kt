package com.kwh.almuniconnect.verification

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.storage.UserLocalModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.storage.UserPreferences



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountVerificationScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val viewModel: PendingVerificationViewModel = viewModel()

    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(initial = UserLocalModel())

    // 🔥 Load Pending using logged-in userId
    LaunchedEffect(user.userId) {
         Log.d("AccountVerificationScreen", "Current userId: ${user.userId}")
        if (user.userId.isNotBlank()) {
            Log.d("AccountVerificationScreen", "Current userId: ${user.userId}")
            Log.d("AccountVerificationScreen", "Current userId: ${user.name}")

            viewModel.loadPending(user.userId)
        } else {
            viewModel.setEmptyState()
        }
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Account Verification",
                showBack = true,
                navController = navController
            )
        }
    ) { paddingValues ->

        when (val state = viewModel.uiState) {

            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Empty -> {
                FallbackMessage("No pending verification alumni")
            }

            is UiState.Error -> {
                FallbackMessage(state.message)
            }

            is UiState.Success -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = state.data,
                        key = { it.alumniId }
                    ) { alumni ->

                        VerificationCard(
                            user = alumni,
                            onApprove = {
                                viewModel.approveAlumni(alumni.alumniId)
                            },
                            onDeny = {
                                viewModel.denyAlumni(alumni.alumniId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FallbackMessage(message: String) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun VerificationCard(
    user: AlumniData,
    onApprove: () -> Unit,
    onDeny: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = user.photoUrl ?: R.drawable.man,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.primary,
                        CircleShape
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            VerificationItem("Name", user.name)
            VerificationItem("Email", user.email ?: "")
            VerificationItem("Mobile", user.mobileNo ?: "")
            VerificationItem("Course", user.courseName ?: "")
            VerificationItem("Batch", user.batch.toString())
            VerificationItem("Company", user.companyName ?: "")
            VerificationItem("Experience", "${user.totalExperience} yrs")

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onApprove,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Text("Approve", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onDeny,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Text("Deny")
            }
        }
    }
}

@Composable
fun VerificationItem(label: String, value: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Text(
            text = value.ifBlank { "-" },
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}
