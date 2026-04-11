package com.kwh.almuniconnect.morefeature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.kwh.almuniconnect.appbar.HBTUTopBar

@Composable
fun NearbyComingSoonScreen(
    navController: NavController,
    onNotifyClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var interestedCount by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("feature_interest")
            .document("nearby")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    interestedCount = snapshot.getLong("count") ?: 0
                }
            }
    }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            HBTUTopBar(
                title = "Nearby",
                navController = navController
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(scrollState),   // ✅ ADD THIS
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            // 🔥 Title
            Text(
                text = "Connect With Alumni\nNear You 📍",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Discover alumni around your location, build real connections and explore nearby opportunities.",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 🔥 Feature Cards (same pattern)
            FeatureItem1(Icons.Default.LocationOn, "Find Nearby Alumni", "See who is around you")
            FeatureItem1(Icons.Default.Groups, "Local Networking", "Meet & grow your network")
            FeatureItem1(Icons.Default.Business, "Nearby Companies", "Explore opportunities nearby")
            FeatureItem1(Icons.Default.Event, "Local Meetups", "Attend alumni meet events")

            Spacer(modifier = Modifier.height(18.dp))

            if (interestedCount > 0) {
                Text(
                    text = "🔥 $interestedCount alumni nearby waiting",
                    fontSize = 13.sp,
                    color = Color(0xFF16A34A),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            // 🔔 Button
            Button(
                onClick = onNotifyClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Notify Me When Live",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Launching Soon • Connect Locally",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}