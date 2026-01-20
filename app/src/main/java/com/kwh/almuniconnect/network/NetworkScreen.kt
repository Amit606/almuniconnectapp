package com.kwh.almuniconnect.network

import AlumniViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar
import kotlin.jvm.java
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(
    navController: NavController,
    onOpenProfile: (AlumniDto) -> Unit = {},
    modifier: Modifier = Modifier
) {

    val apiService = remember {
        NetworkClient.createService(ApiService::class.java)
    }
    val repository = remember { AlumniRepository(apiService) }

    val viewModel: AlumniViewModel = viewModel(
        factory = AlumniViewModelFactory(repository)
    )

    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAlumni()
    }

    TrackScreen("alumni_network_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Networks",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            when (state) {

                is AlumniState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is AlumniState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (state as AlumniState.Error).message,
                            color = Color.Red
                        )
                    }
                }

                is AlumniState.Success -> {

                    val alumniList =
                        (state as AlumniState.Success).alumni
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = alumniList,
                            key = { it.alumniId } // ✅ stable key
                        ) { alumni ->
                            AlumniCard(
                                alumni = alumni,
                                onClick = { onOpenProfile(alumni) }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun AlumniCard(alumni: AlumniDto, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        onClick = onClick, // ✅ ONLY THIS

        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(alumni.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                placeholder = painterResource(R.drawable.man),
                error = painterResource(R.drawable.man),
                fallback = painterResource(R.drawable.man)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(alumni.name,  style = MaterialTheme.typography.titleMedium)
                Text("${alumni.courseName} - ${alumni.batch}",    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A8194))
                Text(
                    alumni.companyName.toString(),    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A8194))
            }

            Button(


                onClick = { openUrl(context, alumni.linkedinUrl.toString()) }) {
                Text("Connect", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

fun openUrl(context: Context, url: String) {
    if (url.isBlank()) return

    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    context.startActivity(intent)
}




