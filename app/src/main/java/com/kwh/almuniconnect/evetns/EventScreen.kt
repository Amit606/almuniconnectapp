package com.kwh.almuniconnect.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// ✅ SINGLE DATA MODEL
data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val color: Color
)

// ✅ SAMPLE DATA
fun sampleEvents(): List<Event> = List(20) {
    Event(
        id = it,
        title = "Event ${it + 1}",
        date = "Dec ${10 + it}, 2025",
        color = listOf(
            Color(0xFF6A80E8),
            Color(0xFF34D399),
            Color(0xFFFBBF24),
            Color(0xFFF87171)
        ).random()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavController) {

    val events = remember { sampleEvents() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Events") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF0E1420))
                .padding(12.dp),
            verticalItemSpacing = 12.dp,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events.size) { index ->
                EventCard(events[index])
            }
        }
    }
}

@Composable
fun EventCard(event: Event) {

    val cardHeight = remember {
        listOf(160.dp, 190.dp, 220.dp).random()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable {
                // TODO: Navigate to event details
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = event.color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = event.title,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Text(
                text = event.date,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 13.sp
            )
        }
    }
}
