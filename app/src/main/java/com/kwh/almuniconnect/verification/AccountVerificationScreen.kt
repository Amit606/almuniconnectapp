package com.kwh.almuniconnect.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountVerificationScreen(
    navController: NavController,
    user: UserLocalModel,
    onApprove: () -> Unit,
    onDeny: () -> Unit
) {

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Account Verification",
                showBack = true,
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // =========================
            // USER DETAILS CARD
            // =========================

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AsyncImage(
                        model = user.photo.ifEmpty { R.drawable.man },
                        contentDescription = null,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    VerificationItem("Name", user.name)
                    VerificationItem("Email", user.email)
                    VerificationItem("Mobile", user.mobile)
                    VerificationItem("Branch", user.branch)
                    VerificationItem("Passout Year", user.year)
                }
            }

            // =========================
            // ACTION BUTTONS
            // =========================

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Button(
                    onClick = onApprove,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    )
                ) {
                    Text("Approve", color = Color.White)
                }

                OutlinedButton(
                    onClick = onDeny,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
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
