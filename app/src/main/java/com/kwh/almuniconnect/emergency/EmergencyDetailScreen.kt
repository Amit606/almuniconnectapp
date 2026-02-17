package com.kwh.almuniconnect.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar

@Composable
fun EmergencyDetailScreen(
    navController: NavController,
    emergency: EmergencyDemo,
    onDonateClick: () -> Unit
) {
    val progress =
        (emergency.raised.toFloat() / emergency.target.toFloat()).coerceIn(0f, 1f)

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Emergency Details",
                navController = navController
            )
        },
        bottomBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(10.dp)
            ) {
                Button(
                    onClick = onDonateClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Donate Now",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // 🔴 Emergency Title
            Text(
                text = emergency.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1E)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 📝 Description
            Text(
                text = emergency.description,
                fontSize = 14.sp,
                color = Color(0xFF3A3A3C),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 💰 Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color(0xFFF9F9F9)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    LinearProgressIndicator(
                        progress = progress,
                        color = Color(0xFFD32F2F),
                        trackColor = Color(0xFFEDEDED),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(Color.Transparent, RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "₹${emergency.raised} raised",
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Goal ₹${emergency.target}",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 👤 Requested By
            Text(
                text = "Requested By",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = emergency.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Batch ${emergency.batch} • ${emergency.department}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(90.dp)) // space for bottom button
        }
    }
}
