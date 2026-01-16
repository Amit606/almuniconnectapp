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
import androidx.navigation.NavController
import com.google.common.math.LinearTransformation.horizontal
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.appbar.HBTUTopBar

// ✅ SINGLE DATA MODEL
data class Event(
    val title: String,
    val location: String,
    val date: String,
    val price: String,
    val image: Int
)

// ✅ SAMPLE DATA
val events = listOf(
    Event(
        "Harcourtian  MCA Alumni Meet 2026",
        "AAIOI, Safdarjung, Delhi",
        "Feb 22, 11:00 AM - 5 PM",
        "Registration Free : 1500 INR",
        R.drawable.first
    ),
    Event(
        "Alumni Networking Lunch",
        "HBTU Main Hall",
        "Mar 05, 01:00 PM",
        "",
        R.drawable.second
    ),
    Event(
        "Career Guidance by Seniors",
        "HBTU Auditorium",
        "Mar 10, 10:30 AM",
        "",
        R.drawable.third
    ),
    Event(
        "Tech Talk: Android Development",
        "Computer Center",
        "Mar 15, 11:00 AM",
        "",
        R.drawable.hbtu
    ),
    Event(
        "Campus Placement Drive",
        "HBTU Placement Cell",
        "Mar 20, 09:00 AM",
        "",
        R.drawable.hbtu
    ),
    Event(
        "MCA Cultural Fest",
        "College Open Ground",
        "Mar 25, 05:00 PM",
        "",
        R.drawable.hbtu
    ),
    Event(
        "Startup & Entrepreneurship Meet",
        "Innovation Hub",
        "Apr 02, 11:00 AM",
        "",
        R.drawable.hbtu
    ),
    Event(
        "Silver Jubilee Alumni Celebration",
        "HBTU Convention Hall",
        "Apr 10, 06:00 PM",
        "",
        R.drawable.hbtu
    ),
    Event(
        "Workshop on Resume & Interview",
        "Training Hall",
        "Apr 15, 10:00 AM",
        "",
        R.drawable.hbtu
    ),
    Event(
        "Annual HBTU Alumni Get-Together",
        "HBTU Campus Lawn",
        "Apr 20, 05:30 PM",
        "",
        R.drawable.hbtu
    )
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Events",
                navController = navController
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item { EventBanner() }

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
                        "${Routes.EVENT_DETAILS}/${event.title}/${event.location}/${event.date}/${event.price}"
                    )
                }
            }
        }
    }
}



@Composable
fun EventBanner() {
    Box(
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.newggg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x66000000))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                "SUN, 22 FEB 2026",
                color = Color.White,
                fontSize = 12.sp
            )
            Text(
                "MCA Alumni Meet & Greet",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
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
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White//Color(0xFFFFF4F1)
        ),
        elevation = CardDefaults.cardElevation(1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(event.image),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    event.location,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        event.date,
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

//
        }
    }
}


