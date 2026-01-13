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
        "HBTU MCA Alumni Meet 2026",
        "AAIOI, Safdarjung, Delhi",
        "Feb 22, 11:00 AM",
        "",
        R.drawable.hbtu
    ),
    Event(
        "Alumni Networking Lunch",
        "HBTU Main Hall",
        "Mar 05, 01:00 PM",
        "",
        R.drawable.hbtu
    ),
    Event(
        "Career Guidance by Seniors",
        "HBTU Auditorium",
        "Mar 10, 10:30 AM",
        "",
        R.drawable.hbtu
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
            TopAppBar(
                title = {
                    Text("Events")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White

                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background( Brush.verticalGradient(
                    listOf(
                        Color(0xFF0D1B2A), // Navy
                        Color(0xFF1B4DB1), // Royal
                        Color(0xFF3A7BD5)  // Light Blue
                    )
                ))
        ) {
//            shape = RoundedCornerShape(12.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = Color(0xFF142338)
//            )
            item { EventBanner() }

            item {
                Text(
                    "Upcoming Events",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
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
    Box(modifier = Modifier.height(220.dp)) {
        Image(
            painter = painterResource(R.drawable.hbtu),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x88000000))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("SUN, 22 Feb 26", color = Color.White, fontSize = 12.sp)
            Text(
                "MCA Alumni Meet & Greet",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun EventCard(event: Event,onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//        .clickable { onClick() },   // 👈 Click enabled
//
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFF142338)
//        )
//    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B2F4B) // Alumni Card Blue
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, Color(0xFF2E4C7D)) // Soft premium border
    ){
        Row(modifier = Modifier.padding(12.dp)) {

            Image(
                painter = painterResource(event.image),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(event.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(event.location, color = Color.Gray, fontSize = 13.sp)

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Text(event.date, color = Color.White,fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(event.price, fontWeight = FontWeight.Bold, color = Color(0xFF7E3FF2))
                Icon(
                    Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

