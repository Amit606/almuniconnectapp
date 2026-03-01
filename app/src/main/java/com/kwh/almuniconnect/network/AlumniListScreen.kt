package com.kwh.almuniconnect.network



import AlumniViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar

@Composable
fun AlumniListScreen(
    navController: NavController,
    branchShort: String,
    year: Int,
    modifier: Modifier = Modifier
) {

    val apiService = remember {
        NetworkClient.createService(ApiService::class.java)
    }

    val repository = remember {
        AlumniRepository(apiService)
    }

    val viewModel: AlumniViewModel = viewModel(
        factory = AlumniViewModelFactory(repository)
    )

    // 🔥 Load data once
    LaunchedEffect(Unit) {
        viewModel.loadAllAlumni()
    }

    val allAlumni = viewModel.allAlumni

    val filteredList = remember(allAlumni, branchShort, year) {
        viewModel.getAlumniByBranchAndYear(branchShort, year)
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "$branchShort - $year",
                navController = navController
            )
        }
    ) { paddingValues ->

        when {
            allAlumni.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            filteredList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Alumni Found")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = filteredList,
                        key = { it.alumniId ?: it.hashCode() }
                    ) { alumni ->

                        AlumniCard1(
                            alumni = alumni,
                            onClick = {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("alumni", alumni)

                                navController.navigate("profile")
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun AlumniCard1(alumni: AlumniDto, onClick: () -> Unit) {

    val context = LocalContext.current

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(alumni.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
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

                Text(
                    text = alumni.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${alumni.courseName} - ${alumni.batch}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A8194)
                )

                Text(
                    text = alumni.companyName ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A8194)
                )
            }

            IconButton(
                onClick = { openUrl(context, alumni.linkedinUrl ?: "") }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_linkedin),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
    }
}