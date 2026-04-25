package com.kwh.almuniconnect.morefeature
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.HourglassTop
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.appbar.HBTUTopBar

sealed class MoreFeature {

    object JobProfile : MoreFeature()
    object Media : MoreFeature()
    object Nearby : MoreFeature()
    object Verification : MoreFeature()

}
fun getMoreFeatures(): List<FeatureUI> {
    return listOf(
        FeatureUI("Job Profile", Icons.Default.Work, MoreFeature.JobProfile),
        FeatureUI("Media", Icons.Default.VideoLibrary, MoreFeature.Media),
        FeatureUI("Nearby Alumni", Icons.Default.LocationOn, MoreFeature.Nearby),
        FeatureUI("Verification", Icons.Default.HourglassTop, MoreFeature.Verification)
    )
}
data class FeatureUI(
    val title: String,
    val icon: ImageVector,
    val action: MoreFeature
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreFeaturesBottomSheet(navController: NavController, viewModel: MoreViewModel = viewModel()) {

    val context = LocalContext.current
    val features = viewModel.features

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            HBTUTopBar(
                title = "Explore More Features",
                navController = navController
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            modifier = Modifier.padding(padding),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(features) { feature ->

                FeatureCard(feature = feature) {
                    viewModel.onFeatureClick(
                        feature.action,
                        context,
                        navController
                    )
                }
            }
        }
    }
}
@Composable
fun FeatureCard(feature: FeatureUI, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE0E7FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = feature.title,
                    tint = Color(0xFF1E3A8A)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = feature.title,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


