package com.kwh.almuniconnect.nearby

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    navController: NavController,
    onAllowClick: () -> Unit,
    onSkipClick: () -> Unit
) {

    val permissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    val screenHeight = LocalConfiguration.current.screenHeightDp

    // 🔥 Auto-redirect if already granted
    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            onAllowClick()
        }
    }

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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LocationAnimation(screenHeight)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Find Nearby Harcourtians",
                fontSize = (screenHeight * 0.03f).sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Discover verified Harcourtians around you for meaningful connections, mentorship, and local meetups.",
                fontSize = (screenHeight * 0.018f).sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "📍 Your location is used only to find nearby Harcourtians — it is never shown to other users.",
                fontSize = (screenHeight * 0.016f).sp,
                color = Color(0xFF424242),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "🔒 With your permission, we store only approximate location (not exact address) to enable nearby discovery. You remain in full control.",
                fontSize = (screenHeight * 0.016f).sp,
                color = Color(0xFF2E7D32),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "✅ Trusted by Harcourtian alumni community",
                fontSize = (screenHeight * 0.015f).sp,
                color = Color(0xFF1E88E5),
                textAlign = TextAlign.Center
            )
            TextButton(
                onClick = {
                    navController.navigate(Routes.LOCATION_PERMISSION_INFO)
                }
            ) {
                Text(
                    text = "Why do we need your location?",
                    color = Color(0xFF1E88E5),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    permissionState.launchPermissionRequest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Allow Location")
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = {
                onSkipClick()
            }) {
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