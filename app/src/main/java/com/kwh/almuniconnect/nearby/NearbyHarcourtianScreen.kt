package com.kwh.almuniconnect.nearby


import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.google.accompanist.permissions.*
var alumniList by mutableStateOf(
    listOf(
        NearAlumni(
            userId = "1",
            name = "Amit Gupta",
            branch = "MCA",
            batch = "2015",
            city = "Delhi",
            profileImage = "",
            latitude = 28.6139,
            longitude = 77.2090,
            distanceKm = 1.2f
        ),
        NearAlumni(
            userId = "2",
            name = "Rahul Singh",
            branch = "Mechanical",
            batch = "2012",
            city = "Noida",
            profileImage = "",
            latitude = 28.5355,
            longitude = 77.3910,
            distanceKm = 3.5f
        )
    )
)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun NearbyHarcourtianScreen(
    viewModel: NearbyAlumniViewModel = viewModel()
) {

    val context = LocalContext.current
    val alumniList = alumniList//viewModel.alumniList

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text("Nearby Harcourtians")
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map View"
                        )
                    }
                }
            )
        }

    ) { padding ->

        LocationPermission {

            val locationProvider = LocationProvider(context)

            LaunchedEffect(Unit) {

                val permission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

                if (permission == PackageManager.PERMISSION_GRANTED) {

                    locationProvider.getLocation { location ->

                        viewModel.loadNearby(
                            location.latitude,
                            location.longitude
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()

                .background(Color(0xFFF5F7FA))
        ) {

            NearbySmartBanner(
                count = alumniList.size
            )

            if (alumniList.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Harcourtians nearby")
                }

            } else {

                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {

                    items(alumniList) { alumni ->

                        AlumniCard(alumni)

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
@Composable
fun AlumniCard(alumni: NearAlumni) {

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
                    onClick = { },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("Connect")
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = { },
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("Chat")
                }
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