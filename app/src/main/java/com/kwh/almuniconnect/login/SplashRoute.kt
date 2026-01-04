package com.kwh.almuniconnect.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.storage.UserPreferences
import androidx.compose.runtime.getValue

@Composable
fun SplashRoute(
    navController: NavController
) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val isLoggedIn by userPrefs.isLoggedIn().collectAsState(initial = false)

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME) { inclusive = true }
            }
        }
    }
}
