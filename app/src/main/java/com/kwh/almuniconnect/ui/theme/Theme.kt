package com.kwh.almuniconnect.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🔵 LinkedIn Inspired Colors
val LinkedBlue = Color(0xFF0A66C2)
val LinkedBlueDark = Color(0xFF004182)
val LinkedBackground = Color(0xFFF3F2EF)
val LinkedSurface = Color(0xFFFFFFFF)

val LinkedTextPrimary = Color(0xFF1F1F1F)
val LinkedTextSecondary = Color(0xFF5F5F5F)
val LinkedDivider = Color(0xFFE0E0E0)

private val LinkedLightColors = lightColorScheme(
    primary = LinkedBlue,
    onPrimary = Color.White,
    background = LinkedBackground,
    surface = LinkedSurface,
    onSurface = LinkedTextPrimary,
    outline = LinkedDivider
)

@Composable
fun LinkedTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LinkedLightColors,
        typography = Typography(),
        content = content
    )
}
