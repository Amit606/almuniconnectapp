package com.kwh.almuniconnect.analytics

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun TrackScreen(screenName: String) {
    LaunchedEffect(screenName) {
        AnalyticsManager.logScreen(screenName)
    }
}
fun Modifier.trackClick(event: AnalyticsEvent): Modifier =
    this.clickable {
        AnalyticsManager.logEvent(event)
    }