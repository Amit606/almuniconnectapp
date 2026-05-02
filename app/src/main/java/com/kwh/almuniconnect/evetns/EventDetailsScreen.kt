package com.kwh.almuniconnect.evetns

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.net.URLDecoder

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

    val horizontalPadding = 20.dp

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Event Details",
                navController = navController
            )
        }
    ) { paddingValues ->
        val decodedTitle = URLDecoder.decode(title, "UTF-8")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            // 🔝 Top Image with Gradient Overlay
            Box {

                val imageRes = when {
                    decodedTitle.contains("Holi Milan", ignoreCase = true) ->
                        R.drawable.ic_holi

                    decodedTitle.contains("Alumni Meet", ignoreCase = true) ->
                        R.drawable.ic_alumni

                    else ->
                        R.drawable.ic_demo_events
                }

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(20.dp)),   // 👈 Add this
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f)
                                )
                            )
                        )
                )
            }

            Spacer(Modifier.height(16.dp))

            // 📝 Title
            Text(
                text = decodedTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )

            Spacer(Modifier.height(6.dp))

            // 📅 Date
            Log.e("EventDetailsScreen", "Decoded Date: $date")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))
                val formattedDate = date.substringBefore("T")
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(6.dp))
            val decodedLocation = URLDecoder.decode(location, "UTF-8")

            // 📍 Location
            Text(
                text = decodedLocation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )

            Spacer(Modifier.height(20.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(Modifier.height(16.dp))

            // 📄 Description
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Join us for an amazing alumni and college event where seniors and juniors come together. Network, celebrate, and relive memories with HBTU family.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )

            Spacer(Modifier.height(24.dp))

            // 📍 Map Section
            Text(
                text = "Location",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )

            Spacer(Modifier.height(12.dp))

            StreetMapViewOSM(location)

            Spacer(Modifier.height(20.dp))

            // 🔵 Open in Google Maps
            Button(
                onClick = { openInGoogleMaps(context, location) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = horizontalPadding)
            ) {
                Text("Open in Google Maps")
            }

            Spacer(Modifier.height(12.dp))

            // 🟢 Share Location
            Button(
                onClick = { shareLocationOnWhatsApp(context, location) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = horizontalPadding)
            ) {
                Text("Share Location")
            }

            Spacer(Modifier.height(24.dp))
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
            .padding(horizontal = 20.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        factory = {

            val map = MapView(it)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.setMultiTouchControls(true)

            val controller = map.controller
            controller.setZoom(15.0)

            val geoPoint = GeoPoint(28.5562, 77.1000)
            controller.setCenter(geoPoint)

            val marker = Marker(map)
            marker.position = geoPoint
            marker.title = address
            map.overlays.add(marker)

            map
        }
    )
}