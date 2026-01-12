// GetStartedScreen.kt
// Replication of the provided "Let's Get Started" card screen in Jetpack Compose

package com.kwh.almuniconnect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight



@Composable
fun GetStartedCard(
    modifier: Modifier = Modifier,
    onJoinNow: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0E1420))
            .windowInsetsPadding(WindowInsets.systemBars) // ✅ handles notch + status + nav bar
    ) {

        // 🔵 Decorative blobs
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-24).dp, y = (-24).dp)
                .clip(RoundedCornerShape(80.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFE6F0FF), Color.Transparent)
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-70).dp, y = 40.dp)
                .clip(RoundedCornerShape(110.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFEFF6FF), Color.Transparent)
                    )
                )
        )

        // 👩 Top avatar
        Image(
            painter = painterResource(id = R.drawable.girl),
            contentDescription = null,
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-40).dp, y = 24.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // 👨 Main avatar
        Image(
            painter = painterResource(id = R.drawable.man),
            contentDescription = null,
            modifier = Modifier
                .size(88.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-24).dp, y = 80.dp)
                .clip(CircleShape)
                .shadow(6.dp, CircleShape),
            contentScale = ContentScale.Crop
        )

        // 📝 Text
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 100.dp) // leave space for button
        ) {
            Text(
                text = "Let's Get",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Started",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Grow Together",
                fontSize = 12.sp,
                color = Color.White
            )
        }

        // 🔘 JOIN NOW button (100% safe on all phones)
        Button(
            onClick = onJoinNow,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF142338)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()   // ✅ keeps above gesture bar
                .padding(bottom = 16.dp)
                .width(160.dp)
                .height(44.dp)
        ) {
            Text("JOIN NOW", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
