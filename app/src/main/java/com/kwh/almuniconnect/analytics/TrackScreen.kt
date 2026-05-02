package com.kwh.almuniconnect.analytics

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun TrackScreen(screenName: String) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        AnalyticsManager.logScreen(context,screenName)
    }
}
