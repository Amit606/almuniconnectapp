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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.kwh.almuniconnect.appbar.HBTUTopBar

@Composable
fun JobProfileComingSoonScreen(
    navController: NavController,
    onNotifyClick: () -> Unit
) {
    var interestedCount by remember { mutableStateOf(0L) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("feature_interest")
            .document("job_profile")
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
                title = "Job Profile",
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
                text = "Build Your\nProfessional Identity 🚀",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Create your job profile, upload resume and connect with alumni for real opportunities.",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 🔥 Feature Cards
            FeatureItem1(Icons.Default.Person, "Create Profile", "Student & Alumni profiles")
            FeatureItem1(Icons.Default.Description, "Upload Resume", "Show your skills & experience")
            FeatureItem1(Icons.Default.Chat, "Connect Alumni", "Direct interaction & mentorship")
            FeatureItem1(Icons.Default.Work, "Get Opportunities", "Jobs & referrals from network")

            Spacer(modifier = Modifier.height(28.dp))

            if (interestedCount > 0) {
                Text(
                    text = "🔥 $interestedCount alumni already waiting",
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
                    containerColor = Color(0xFF2563EB) // Blue
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
                text = "Launching Soon • Alumni Powered Careers",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun FeatureItem1(icon: ImageVector, title: String, desc: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FAFB)
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    color = Color(0xFF111827),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = desc,
                    color = Color(0xFF6B7280),
                    fontSize = 12.sp
                )
            }
        }
    }
}