package com.kwh.almuniconnect.network

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
            TopAppBar(
                title = { Text("Alumni Branches") }
            )
        }
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            branches.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
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
                        .padding(padding)
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
                                    "year/${branch.id}/${branch.shortName}"
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

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.3f)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = getBranchIcon(branch.shortName),
                contentDescription = branch.name,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = branch.shortName,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = branch.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
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