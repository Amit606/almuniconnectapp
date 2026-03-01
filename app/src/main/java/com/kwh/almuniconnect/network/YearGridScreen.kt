package com.kwh.almuniconnect.network
import AlumniViewModel
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes
import java.util.Calendar

data class YearUiModel(
    val year: String,
    val alumniCount: Int
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearGridScreen(
    navController: NavController,
    branchId: Int,
    branchShort: String,
    viewModel: AlumniViewModel
) {

    // 🔥 Load once
    LaunchedEffect(Unit) {
        viewModel.loadAllAlumni()
    }

    // 🔥 Observe state
    val allAlumni = viewModel.allAlumni

    // 🔥 Derived state (important)
    val years = remember(allAlumni, branchShort) {
        viewModel.getYearsByBranch(branchShort)
    }

   // val years = viewModel.getYearsByBranch(branchShort)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("$branchShort Alumni")
                }
            )
        }
    ) { paddingValues ->

        if (years.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No Alumni Found")
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 110.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(years, key = { it.year }) { yearItem ->

                    PremiumYearGridItem(
                        yearItem = yearItem,
                        onClick = {
                            navController.navigate("alumni/${branchShort}/${yearItem.year}")
//                            navController.navigate(
//                                "alumni/${branchShort}/${yearItem.year}"
//                            )
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
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSilver)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
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

            // Year Center Text
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = yearItem.year,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${yearItem.alumniCount} Alumni",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Badge


            // Silver Jubilee Ribbon
            if (isSilver) {
                Text(
                    text = "🎉 25 Years",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}