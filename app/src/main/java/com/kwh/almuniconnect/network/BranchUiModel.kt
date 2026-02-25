package com.kwh.almuniconnect.network

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// 🔹 Demo Model
data class BranchUiModel(
    val name: String,
    val alumniCount: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BranchGridDemoScreen(navController: NavController) {

    // 🔥 Static Demo Data
    val branches = listOf(
        BranchUiModel("MCA", 1240),
        BranchUiModel("B.Tech CS", 3250),
        BranchUiModel("IT", 890),
        BranchUiModel("ECE", 1450),
        BranchUiModel("ME", 980),
        BranchUiModel("CE", 720)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Branch") }
            )
        }
    ) { paddingValues ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(branches) { branch ->

                BranchGridItem(
                    branch = branch,
                    onClick = {
                        navController.navigate("year/${branch.name}")

                    }
                )
            }
        }
    }
}

@Composable
fun BranchGridItem(
    branch: BranchUiModel,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier.aspectRatio(1f)
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔵 Alumni Count Badge
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Text(
                    text = if (branch.alumniCount > 999)
                        "${branch.alumniCount}+"
                    else
                        branch.alumniCount.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                )
            }

            // 🎓 Branch Name
            Text(
                text = branch.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}