package com.kwh.almuniconnect.profile

import AlumniViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.runtime.getValue

@Composable
fun AlumniProfileRoute(
    alumniId: String,
    navController: NavController,
    viewModel: AlumniViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(alumniId) {
        // If list is empty, load it
        if (state !is AlumniState.Success) {
            viewModel.loadAlumni()
        }
    }

    val alumni = (state as? AlumniState.Success)
        ?.alumni
        ?.firstOrNull { it.alumniId == alumniId }

    when {
        alumni != null -> {
            AlumniProfileScreen(
                alumni = alumni,
                navController = navController
            )
        }

        state is AlumniState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state is AlumniState.Error -> {
            Text(
                text = (state as AlumniState.Error).message,
                color = Color.Red
            )
        }

        else -> {
            Text("Profile not found")
        }
    }
}
