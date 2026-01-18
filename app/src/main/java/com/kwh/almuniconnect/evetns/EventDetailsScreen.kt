package com.kwh.almuniconnect.evetns

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    navController: NavController,
    title: String,
    location: String,
    date: String,
    price: String
) {
    val context = LocalContext.current
    TrackScreen("event_details_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Events Details",
                navController = navController
            )
        }
    ) { paddingValues ->


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                   // .background(Color(0xFF0E1420))
            ) {

                // 🔝 Top Image with Date Badge
                Box {
                    Image(
                        painter = painterResource(R.drawable.newggg),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentScale = ContentScale.Crop
                    )



                }

                // 📝 Event Title & Favorite
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        title,
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )


                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        date,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )


                }

                // 📍 Location & Rating
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(location, style = MaterialTheme.typography.titleMedium,
                    )
                }



                // 📄 Description
                Text(
                    "Description",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    "Join us for an amazing alumni and college event where seniors and juniors come together. Network, celebrate, and relive memories with HBTU family.",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(20.dp))

                // 📍 Location Map (Free OpenStreetMap)
                Text(
                    "Location",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,

                    modifier = Modifier.padding(16.dp)
                )

                StreetMapViewOSM(location)

                Spacer(Modifier.height(16.dp))

                // 🔵 Open in Google Maps
                Button(
                    onClick = { openInGoogleMaps(context, location) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4))
                ) {
                    Text("Open in Google Maps",  style = MaterialTheme.typography.titleMedium,)
                }

                Spacer(Modifier.height(10.dp))

                // 🟢 Share Location WhatsApp
                Button(
                    onClick = { shareLocationOnWhatsApp(context, location) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Text("Share Location on WhatsApp", style = MaterialTheme.typography.titleMedium,
                    )
                }

                Spacer(Modifier.height(20.dp))
            }
        }

    }

fun openInGoogleMaps(context: Context, address: String) {
    val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(address)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // If Google Maps app not installed
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}
fun shareLocationOnWhatsApp(context: Context, location: String) {
    val mapsLink = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(location)}"
    val message = "📍 Event Location:\n$location\n\nOpen in Maps 👇\n$mapsLink"

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
        setPackage("com.whatsapp")
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // WhatsApp not installed – open share chooser
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share location"))
    }
}
@Composable
fun StreetMapViewOSM(address: String) {


    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(16.dp)),
        factory = {

            val map = MapView(it)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.setMultiTouchControls(true)

            val controller = map.controller
            controller.setZoom(15.0)

            // Default location (Delhi)
            val geoPoint = GeoPoint(28.5562, 77.1000)
            controller.setCenter(geoPoint)

            // Marker
            val marker = Marker(map)
            marker.position = geoPoint
            marker.title = address
            map.overlays.add(marker)

            map
        }
    )
}

