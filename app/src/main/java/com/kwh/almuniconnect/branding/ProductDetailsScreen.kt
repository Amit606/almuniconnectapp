package com.kwh.almuniconnect.branding

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
fun ProductDetailsScreen(
    navController: NavController,
    title: String,
    location: String,
    date: String,
    price: String
) {
    val context = LocalContext.current
    TrackScreen("product_details_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Service Details",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            // 🌄 Hero Image with Gradient
            Box {
                Image(
                    painter = painterResource(R.drawable.newggg),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    InfoChip(text = date)
                }
            }

            Spacer(Modifier.height(16.dp))

            // 📌 Info Card
            InfoCard {
                InfoRow("📍 Location", location)
                InfoRow("💰 Price", price)
            }

            // 📄 Description
            SectionTitle("Description")
            Text(
                text = "Join us for an amazing alumni and college event where seniors and juniors come together. Network, celebrate, and relive memories with HBTU family.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(20.dp))

            // 🗺 Map Section
            SectionTitle("Location Map")

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                StreetMapViewOSM(location)
            }

            Spacer(Modifier.height(20.dp))

            // 🔵 Actions
            PrimaryButton(
                text = "Open in Google Maps",
                color = Color(0xFF4285F4)
            ) {
                openInGoogleMaps(context, location)
            }

            SecondaryButton(
                text = "Share Location on WhatsApp",
                color = Color(0xFF25D366)
            ) {
                shareLocationOnWhatsApp(context, location)
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
@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(16.dp)
    )
}
@Composable
fun InfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.SemiBold)
        Text(value)
    }
    Spacer(Modifier.height(8.dp))
}
@Composable
fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .background(
                Color.White.copy(alpha = 0.2f),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}
@Composable
fun PrimaryButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text, fontSize = 16.sp)
    }
}

@Composable
fun SecondaryButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text, fontSize = 16.sp)
    }
}

