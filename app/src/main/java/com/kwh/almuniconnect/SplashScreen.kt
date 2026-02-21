package com.kwh.almuniconnect

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
import com.kwh.almuniconnect.storage.UserSession
import com.kwh.almuniconnect.verification.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {

    val scale = remember { Animatable(0.7f) }
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(80f) }
    val pulse = remember { Animatable(1f) }

    val context = LocalContext.current
    val isLoggedIn by UserSession.isLoggedIn(context).collectAsState(initial = false)

    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(initial = UserLocalModel())

    val splashViewModel: SplashViewModel = viewModel()

    // 🚀 Main Splash Logic
    LaunchedEffect(user.userId, isLoggedIn) {

        // Run animation
        launch {
            scale.animateTo(
                1f,
                animationSpec = tween(900, easing = FastOutSlowInEasing)
            )
        }

        launch {
            alpha.animateTo(
                1f,
                animationSpec = tween(800, delayMillis = 400)
            )
        }

        launch {
            offsetY.animateTo(
                0f,
                animationSpec = tween(900, delayMillis = 400)
            )
        }

        launch {
            pulse.animateTo(
                1.08f,
                animationSpec = infiniteRepeatable(
                    animation = tween(900),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }

        delay(1800)

        // 🔥 Navigation Decision

        if (!isLoggedIn) {
            navController.navigate(Routes.INTRO) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
            return@LaunchedEffect
        }

        if (user.userId.isBlank()) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
            return@LaunchedEffect
        }

        splashViewModel.checkAlumniVerification(user.userId) { isVerified ->


            if (isVerified) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            } else {
                navController.navigate(Routes.APPROVAL_PENDING) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }
    }

    // 🎨 UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D1B2A),
                        Color(0xFF1B4DB1),
                        Color(0xFF3A7BD5)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                painter = painterResource(id = R.drawable.playstore),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value * pulse.value)
                    .alpha(alpha.value)
                    .offset(y = offsetY.value.dp)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Harcourtian Alumni Connect",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(alpha.value)
            )

            Text(
                "Your Professional Alumni Network",
                color = Color(0xFFD6E3FF),
                fontSize = 14.sp,
                modifier = Modifier.alpha(alpha.value)
            )
        }
    }
}