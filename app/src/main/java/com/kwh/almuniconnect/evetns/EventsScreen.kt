package com.kwh.almuniconnect.evetns

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwh.almuniconnect.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.common.math.LinearTransformation.horizontal
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.utils.CommonEmptyState
import com.kwh.almuniconnect.utils.encodeRoute

sealed class EventsUiState {
    object Loading : EventsUiState()
    data class Success(val events: List<Event>) : EventsUiState()
    data class Error(val message: String) : EventsUiState()
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: EventsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    TrackScreen("events_screen")

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Events",
                navController = navController
            )
        }
    ) { paddingValues ->

        when (uiState) {

            is EventsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is EventsUiState.Error -> {
                CommonEmptyState(
                    title = "No Upcoming Events",
                    message = "There are no upcoming events right now.\nPlease check back later.",
                    lottieRes = R.raw.no_events,
                    actionText = "Refresh",
                    onActionClick = { viewModel.loadEvents() }
                )
            }

            is EventsUiState.Success -> {
                val events = (uiState as EventsUiState.Success).events

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                   // item { EventBanner() }

                    item {
                        Text(
                            "Upcoming Events",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(events) { event ->
                        EventCard(event) {
                            navController.navigate(
                                "${Routes.EVENT_DETAILS}?title=${event.title.encodeRoute()}&description=${event.description.encodeRoute()}&date=${event.startAt.encodeRoute()}&location=${event.location.encodeRoute()}"
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🔥 Safer image selection logic
            val imageRes = when {
                event.title.contains("Holi", ignoreCase = true) ->
                    R.drawable.ic_holi

                event.title.contains("Alumni", ignoreCase = true) ||
                        event.title.contains("Almuni", ignoreCase = true) ->
                    R.drawable.ic_alumni

                else ->
                    R.drawable.ic_demo_events
            }

            // ✅ Correct Image (Fixed size for Row layout)
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = event.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}



