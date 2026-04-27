package com.kwh.almuniconnect.nearby

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.billing.BillingViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyHarcourtianScreen(
    navController: NavController,
) {

    val context = LocalContext.current
    val application = context.applicationContext as Application

    // ✅ FIXED BillingViewModel (CRASH RESOLVED)
    val billingViewModel: BillingViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )

    val isPremium by billingViewModel.isPremium.collectAsState()

    // ✅ Existing ViewModel
    val viewModel: NearbyAlumniViewModel = viewModel(
        factory = NearbyViewModelFactory(context)
    )

    val alumniList by viewModel.alumniList
    val isLoading by viewModel.isLoading
    val error by viewModel.errorMessage
    var isLocationLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {


        coroutineScope {

            // 🔥 Run billing in parallel
            launch {
                billingViewModel.startBilling()
            }
            launch {
                val locationProvider = LocationProvider(context)
                val location = locationProvider.getLocationSuspend()
                isLocationLoading = false

                location?.let {
                    viewModel.loadNearby(it.latitude, it.longitude)
                }
            }


        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Alumni") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {

            NearbySmartBanner(count = alumniList.size)

            when {
                isLoading -> Loader()
                error != null -> ErrorState(error!!)
                alumniList.isEmpty() -> EmptyState()
                else -> AlumniList(alumniList, navController, isPremium)
            }
        }
    }
}

@Composable
fun Loader() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(msg: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(msg)
    }
}

@Composable
fun EmptyState() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.People, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text("No Harcourtians found nearby", fontWeight = FontWeight.Bold)
        Text(
            "Try moving to a different location",
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}

@Composable
fun AlumniList(
    list: List<NearAlumni>,
    navController: NavController,
    isPremium: Boolean
) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(list) { alumni ->
            AlumniCard(alumni, navController, isPremium)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun AlumniCard(
    alumni: NearAlumni,
    navController: NavController,
    isPremium: Boolean
) {

    var showSheet by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = alumni.profileImage,
                contentDescription = null,
                placeholder = painterResource(R.drawable.man),
                error = painterResource(R.drawable.man),
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {

                Text(alumni.name, fontWeight = FontWeight.Bold)

                Text(
                    "${alumni.branch} • ${alumni.batch}",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Text(
                    "📍 ${String.format("%.1f", alumni.distanceKm)} km",
                    color = Color(0xFF1E88E5),
                    fontSize = 13.sp
                )
            }

            Button(onClick = { showSheet = true }) {
                Text("Connect")
            }
        }
    }

    if (showSheet) {
        AlumniActionSheet(
            alumni = alumni,
            isPremium = isPremium,
            onDismiss = { showSheet = false },
            navController = navController
        )
    }
}

@Composable
fun NearbySmartBanner(count: Int) {

    if (count == 0) return

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(Color(0xFFE3F2FD))
    ) {

        Row(Modifier.padding(16.dp)) {

            Text("👋", fontSize = 24.sp)

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text("$count Harcourtians near you", fontWeight = FontWeight.Bold)
                Text("Connect with alumni around you", fontSize = 13.sp)
            }
        }
    }
}