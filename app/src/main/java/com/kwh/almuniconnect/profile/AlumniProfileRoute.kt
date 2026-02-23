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
  //  val state by viewModel.state.collectAsState()

//    LaunchedEffect(alumniId) {
//        viewModel.loadAlumni()
//    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

//        when (val currentState = state) {
//
//            is AlumniState.Loading -> {
//                CircularProgressIndicator()
//            }
//
//            is AlumniState.Success -> {
//
//                val alumni = currentState.alumni
//                    .firstOrNull { it.alumniId == alumniId }
//
//                if (alumni != null) {
//                    AlumniProfileScreen(
//                        alumni = alumni,
//                        navController = navController
//                    )
//                } else {
//                    Text("Profile not found")
//                }
//            }
//
//            is AlumniState.Error -> {
//                Text(
//                    text = currentState.message,
//                    color = Color.Red
//                )
//            }
//        }
    }
}