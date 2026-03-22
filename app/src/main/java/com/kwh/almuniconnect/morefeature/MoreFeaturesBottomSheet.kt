package com.kwh.almuniconnect.morefeature
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.HourglassTop
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.analytics.TrackScreen
import androidx.core.net.toUri
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.subscription.FeatureCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreFeaturesScreen(navController: NavController) {

    val features = listOf(
        FeatureModel("Mentorship", Icons.Default.School),
        FeatureModel("Thoughts", Icons.Default.Edit),
        FeatureModel("Chat", Icons.Default.Chat),
        FeatureModel("Media", Icons.Default.VideoLibrary),
        FeatureModel("Nearby", Icons.Default.LocationOn),
        FeatureModel("Pending Verification", Icons.Default.HourglassTop) // ✅ better icon
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("More Features 🚀") }
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(features) { feature ->

                FeatureCard(feature = feature) {

                    when (feature.title) {
                        "Mentorship" -> navController.navigate("mentorship")
                        "Thoughts" -> navController.navigate("feed")
                        "Chat" -> navController.navigate("chat")
                        "Media" -> navController.navigate(Routes.MEDIA_FEATURE)
                        "Nearby" -> navController.navigate(Routes.NEARBY_HARCOURTIANS)
                        "Pending Verification" -> navController.navigate(Routes.VERIFICATION) // ✅ ADD THIS

                    }

                }
            }
        }
    }
}
@Composable
fun FeatureCard(feature: FeatureModel, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                modifier = Modifier.size(36.dp),
                tint = Color(0xFF4A90E2)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = feature.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

data class FeatureModel(
    val title: String,
    val icon: ImageVector
)
fun getFeatureList(): List<FeatureModel> {
    return listOf(
        FeatureModel("Mentorship", Icons.Default.School),
        FeatureModel("Thoughts", Icons.Default.Edit),
        FeatureModel("Chat", Icons.Default.Chat),
        FeatureModel("Media", Icons.Default.VideoLibrary),
        FeatureModel("Nearby", Icons.Default.LocationOn)
    )
}