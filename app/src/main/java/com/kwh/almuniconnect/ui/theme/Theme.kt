package com.kwh.almuniconnect.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.kwh.almuniconnect.ui.theme.Typography


private val HBTUColorScheme = lightColorScheme(
    primary = HBTURoyalBlue,
    onPrimary = Color.White,

    secondary = HBTUSageGreen,
    onSecondary = Color.Black,

    error = HBTURacingRed,
    onError = Color.White,

    background = Color.White,
    onBackground = HBTUDarkText,

    surface = Color.White,
    onSurface = HBTUDarkText
)

@Composable
fun AlumniConnectTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HBTUColorScheme,
        typography = Typography(),
        content = content
    )
}