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

data class YearUiModel(
    val year: String,
    val alumniCount: Int
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearGridScreen(
    branchName: String,
    navController: NavController
) {

    // 🔥 Demo static data
    val years = listOf(
        YearUiModel("2022", 320),
        YearUiModel("2021", 285),
        YearUiModel("2020", 260),
        YearUiModel("2019", 240),
        YearUiModel("2018", 198),
        YearUiModel("2017", 175)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$branchName - Select Year") }
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

            items(years) { yearItem ->
                YearGridItem(
                    yearItem = yearItem,
                    onClick = {
                        navController.navigate(
                            "alumni/$branchName/${yearItem.year}"
                        )
                    }
                )
            }
        }
    }
}
@Composable
fun YearGridItem(
    yearItem: YearUiModel,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier.aspectRatio(1f)
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Text(
                    text = yearItem.alumniCount.toString(),
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                )
            }

            Text(
                text = yearItem.year,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}