package com.kwh.almuniconnect.morefeature// 🔥 Compose Core
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

// 🔥 Material 3
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications

// 🔥 Runtime
import androidx.compose.runtime.Composable

// 🔥 UI
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar


@Composable
fun ComingSoonScreen(navController: NavController,onNotifyClick: () -> Unit) {

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Coming Soon",
                navController = navController
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {

                // 🔥 Title
                Text(
                    text = "Something Amazing\nis Coming",
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 40.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 📄 Subtitle
                Text(
                    text = "We’re building something special for your community.\nBe the first to know when we launch.",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 🔔 Notify Button
                Button(
                    onClick = onNotifyClick,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Notify Me",
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}