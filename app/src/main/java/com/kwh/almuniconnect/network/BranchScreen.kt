package com.kwh.almuniconnect.network

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchScreen(
    navController: NavController,
    viewModel: BranchViewModel
) {

    val branches by viewModel.branches.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()   // 👈 Add this in VM

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Branches",
                navController = navController,
            )
        }
    ) { paddingValues ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            branches.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No Branches Found",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 140.dp),
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(
                        items = branches,
                        key = { it.id }
                    ) { branch ->

                        BranchCard(
                            branch = branch,
                            onClick = {
                                navController.navigate(
                                    "year/${branch.id}/${branch.name}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BranchCard(
    branch: Branch,
    onClick: () -> Unit
) {

    val iconColor = getBranchColor(branch.shortName)

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /* -------- ICON CONTAINER -------- */

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        iconColor.copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = getBranchIcon(branch.shortName),
                    contentDescription = branch.name,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            /* -------- SHORT NAME -------- */

            Text(
                text = branch.shortName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            /* -------- FULL NAME -------- */

            if (branch.name.isNotEmpty()) {

                Text(
                    text = branch.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
@Composable
fun getBranchIcon(shortName: String): ImageVector {

    return when (shortName) {

        "CS", "IT", "MCA" -> Icons.Default.Computer
        "EL" -> Icons.Default.Bolt
        "EC" -> Icons.Default.Memory
        "ME" -> Icons.Default.Build
        "CIV" -> Icons.Default.Apartment
        "CH", "BIO" -> Icons.Default.Science
        "FOOD" -> Icons.Default.Restaurant
        "OIL" -> Icons.Default.LocalGasStation
        "PNT" -> Icons.Default.Brush
        "PLA" -> Icons.Default.Category
        "MBA" -> Icons.Default.Business
        "M.Tech" -> Icons.Default.School

        else -> Icons.Default.School
    }
}
@Composable
fun getBranchColor(shortName: String): Color {

    return when (shortName) {

        "CS", "IT", "MCA" -> Color(0xFF2962FF)   // Blue

        "EL" -> Color(0xFFFFC107)                // Amber

        "EC" -> Color(0xFF00BCD4)                // Cyan

        "ME" -> Color(0xFFFF6D00)                // Orange

        "CIV" -> Color(0xFF795548)               // Brown

        "CH", "BIO" -> Color(0xFF4CAF50)         // Green

        "FOOD" -> Color(0xFFE91E63)              // Pink

        "OIL" -> Color(0xFF607D8B)               // Blue Grey

        "PNT" -> Color(0xFF9C27B0)               // Purple

        "PLA" -> Color(0xFF3F51B5)               // Indigo

        "MBA" -> Color(0xFF009688)               // Teal

        "M.Tech" -> Color(0xFFD32F2F)            // Red

        else -> MaterialTheme.colorScheme.primary
    }
}