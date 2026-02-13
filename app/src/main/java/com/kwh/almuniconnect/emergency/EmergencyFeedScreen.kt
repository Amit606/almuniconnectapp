package com.kwh.almuniconnect.emergency
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyFeedScreen(
    navController: NavController,
    onItemClick: (EmergencyDemo) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Emergency Feed",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.White)
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            demoEmergencyList.forEach {
                EmergencyFeedCard(it, onItemClick)
            }
        }
    }
}

@Composable
fun EmergencyFeedCard(
    emergency: EmergencyDemo,
    onClick: (EmergencyDemo) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick(emergency) },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFFE6E9F0)),

        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        emergency.name,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Batch ${emergency.batch} • ${emergency.department}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (emergency.verified) {
                    Text(
                        "✔ Verified",
                        color = Color(0xFF2E7D32),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(emergency.description, maxLines = 2)

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = emergency.raised.toFloat() / emergency.target,
                color = Color(0xFFD32F2F),
                trackColor = Color(0xFFFFCDD2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "₹${emergency.raised} / ₹${emergency.target}",
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onClick(emergency) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Donate Now", color = Color.White)
            }
        }
    }
}

