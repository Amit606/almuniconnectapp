package com.kwh.almuniconnect

import android.R.interpolator.overshoot
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import com.kwh.almuniconnect.storage.UserSession
//import com.airbnb.lottie.compose.LottieAnimation
//import com.airbnb.lottie.compose.LottieCompositionSpec
//import com.airbnb.lottie.compose.rememberLottieComposition
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

    LaunchedEffect(isLoggedIn) {

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

        if (isLoggedIn) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.INTRO) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D1B2A), // Alumni Navy
                        Color(0xFF1B4DB1), // Royal Blue
                        Color(0xFF3A7BD5)  // Light Blue
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
                "AlumniConnect",
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
