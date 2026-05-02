package com.kwh.almuniconnect.morefeature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar

@Composable
fun ComingSoonScreen(
    navController: NavController,
    onNotifyClick: () -> Unit
) {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E293B),
            Color(0xFF334155)
        )
    )

    Scaffold(
        containerColor = Color.Transparent,
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
                .background(gradient)
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 Title
            Text(
                text = "Build Your\nProfessional Identity 🚀",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Create your job profile, upload resume and connect with alumni for real opportunities.",
                fontSize = 14.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 🔥 Feature Cards
            FeatureItem(Icons.Default.Person, "Create Profile", "Student & Alumni profiles")
            FeatureItem(Icons.Default.Description, "Upload Resume", "Show your skills & experience")
            FeatureItem(Icons.Default.Chat, "Connect Alumni", "Direct interaction & mentorship")
            FeatureItem(Icons.Default.Work, "Get Opportunities", "Jobs & referrals from network")

            Spacer(modifier = Modifier.weight(1f))

            // 🔔 Button
            Button(
                onClick = onNotifyClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF22C55E)
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Notify Me When Live")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Launching Soon • Build your future with Alumni Network",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun FeatureItem(icon: ImageVector, title: String, desc: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF22C55E),
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = desc,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}