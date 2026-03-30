package com.kwh.almuniconnect.nearby

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kwh.almuniconnect.R

@Composable
fun LocationPermissionScreen(
    navController: NavController,
    onAllowClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), // ✅ scroll for small devices
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LocationAnimation(screenHeight)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Find Nearby Alumni",
                fontSize = (screenHeight * 0.03f).sp, // ✅ dynamic text
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We use your location to help you discover alumni nearby, attend local meetups, and build stronger connections within your community.",
                fontSize = (screenHeight * 0.018f).sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your location is secure and never shared publicly.",
                fontSize = (screenHeight * 0.015f).sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    Log.d("LocationPermission", "Allow Location clicked")
                    onAllowClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Allow Location")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onSkipClick) {
                Text("Skip for now")
            }
        }
    }
}
@Composable
fun LocationAnimation(screenHeight: Int) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.research)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val size = (screenHeight * 0.25f).dp // ✅ dynamic size

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.size(size)
    )
}