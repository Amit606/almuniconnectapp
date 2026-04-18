package com.kwh.almuniconnect.nearby


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.billing.BillingViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun NearbyHarcourtianScreen(
    navController: NavController,
    viewModel: NearbyAlumniViewModel = viewModel()
) {

    val context = LocalContext.current

    val alumniList by viewModel.alumniList
    val isLoading by viewModel.isLoading
    val error by viewModel.errorMessage
    val billingViewModel: BillingViewModel = viewModel()
    val isPremium by billingViewModel.isPremium.collectAsState()

    var hasLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        billingViewModel.startBilling()
    }

    if (isPremium) {
        Text("Premium User ✅")
    } else {
        Text("Locked 🔒")
    }
    // ✅ SAFE API CALL (only once)
    LaunchedEffect(Unit) {
        if (!hasLoaded) {
            hasLoaded = true

            val locationProvider = LocationProvider(context)

            locationProvider.getLocation { location ->
                location?.let {
                    viewModel.loadNearby(it.latitude, it.longitude)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Harcourtians") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Map, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {

            NearbySmartBanner(count = alumniList.size)

            when {
                isLoading -> Loader()
                error != null -> ErrorState(error!!)
                alumniList.isEmpty() -> EmptyState()
                else -> AlumniList(alumniList,navController,isPremium)
            }
        }
    }
}
@Composable
fun Loader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
@Composable
fun ErrorState(msg: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(msg)
    }
}
@Composable
fun AlumniList(list: List<NearAlumni>,navController: NavController,isPremium: Boolean) {

    LazyColumn(contentPadding = PaddingValues(16.dp)) {

        items(list) { alumni ->

            AlumniCard(alumni,navController, isPremium )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.People,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "No Harcourtians found nearby",
            fontWeight = FontWeight.Bold
        )

        Text(
            "Try moving to a different location or check back later",
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}
@Composable
fun AlumniCard(alumni: NearAlumni,navController: NavController,isPremium: Boolean) {
    var showSheet by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = alumni.profileImage,
                contentDescription = "Profile Image",
                placeholder = painterResource(R.drawable.man),
                error = painterResource(R.drawable.man),
                fallback = painterResource(R.drawable.man),
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = alumni.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = "${alumni.branch} • ${alumni.batch}",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "📍 ${String.format("%.1f", alumni.distanceKm)} km away",
                    color = Color(0xFF1E88E5),
                    fontSize = 13.sp
                )
            }

            Column {

                Button(
                    onClick = {
                        showSheet = true

                    },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("Connect")
                }

                Spacer(modifier = Modifier.height(6.dp))

//                OutlinedButton(
//                    onClick = { },
//                    shape = RoundedCornerShape(10.dp),
//                    contentPadding = PaddingValues(horizontal = 12.dp)
//                ) {
//                    Text("Chat")
//                }
            }
            if (showSheet) {
                AlumniActionSheet(
                    alumni = alumni,
                    isPremium =isPremium ,
                    onDismiss = { showSheet = false },
                    navController = navController
                )
            }
        }
    }
}
@Composable
fun NearbySmartBanner(count: Int) {

    if (count == 0) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "👋",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {

                Text(
                    "$count Harcourtians near you today",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Connect with alumni around you",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}

