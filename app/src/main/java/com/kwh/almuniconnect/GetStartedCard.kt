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
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFF0E1420))
            .windowInsetsPadding(WindowInsets.statusBars) // 👈 adds padding at top
    ) {
        // Decorative large rounded shapes (light blue blobs)
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
        ) {}

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
        ) {}

        // Small top avatar
        Image(
            painter = painterResource(id = R.drawable.girl),
            contentDescription = "avatar",
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-40).dp, y = 24.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        // Larger center avatar overlapping
        Image(
            painter = painterResource(id = R.drawable.man),
            contentDescription = "avatar",
            modifier = Modifier
                .size(88.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-24).dp, y = 80.dp)
                .clip(CircleShape)
                .shadow(6.dp, CircleShape),
            contentScale = ContentScale.Crop
        )

        // Text content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 80.dp)
        ) {
            Text(
                text = "Let's Get",
                fontSize = 28.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Started",
                fontSize = 36.sp,
                color = Color.White,

                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Grow Together",
                fontSize = 12.sp,
                color = Color.White,
            )
        }

        // Join Now button
        Button(
            onClick = onJoinNow,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF142338)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .width(140.dp)
                .height(40.dp)
        ) {
            Text(text = "JOIN NOW", color = Color.White)
        }
    }
}

