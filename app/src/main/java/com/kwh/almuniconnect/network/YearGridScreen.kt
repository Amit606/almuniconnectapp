package com.kwh.almuniconnect.network

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar
import java.util.Calendar

data class YearUiModel(
    val year: String,
    val alumniCount: Int
)

@Composable
fun YearGridScreen(
    navController: NavController,
    branchId: Int,
    branchShort: String,
    viewModel: AlumniBatchViewModel
) {

    LaunchedEffect(branchId) {
        viewModel.loadAlumniBatchCount(branchId)
    }

    val batchList = viewModel.batchList.sortedByDescending { it.batch }
    val loading = viewModel.isLoading

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = branchShort,
                navController = navController
            )
        }
    ) { padding ->

        if (loading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Adaptive(140.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(batchList, key = { it.batch }) { item ->

                    val yearUi = YearUiModel(
                        year = item.batch.toString(),
                        alumniCount = item.count
                    )

                    PremiumYearGridItem(
                        yearItem = yearUi,
                        onClick = {
                            navController.navigate("alumni/$branchShort/${item.batch}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumYearGridItem(
    yearItem: YearUiModel,
    onClick: () -> Unit
) {

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val isSilver = (currentYear - yearItem.year.toInt()) == 25

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = ""
    )

    Card(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSilver)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = yearItem.year,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${yearItem.alumniCount} Alumni",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSilver) {
                Text(
                    text = "🎉 25 Years",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}