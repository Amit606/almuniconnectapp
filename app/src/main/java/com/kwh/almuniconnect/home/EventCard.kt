package com.kwh.almuniconnect.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.evetns.Event

//@Composable
//fun EventCard(event: Event, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .width(220.dp)
//            .height(120.dp)
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(20.dp),
//        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//
//       // Soft premium border
//    ) {
//        Column(modifier = Modifier.padding(12.dp)) {
//            Text(event.title,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(event.date,
//                style = MaterialTheme.typography.bodyMedium,
//                color = Color(0xFF4A4F5A)
//            )
//            Spacer(modifier = Modifier.weight(1f))
//            // Location
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    imageVector = Icons.Default.LocationOn,
//                    contentDescription = null,
//                    tint = Color(0xFF7A8194),
//                    modifier = Modifier.size(16.dp)
//                )
//
//                Spacer(modifier = Modifier.width(6.dp))
//
//                Text(
//                    text = event.location,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color(0xFF7A8194)
//                )
//            }
//        }
//    }
//}
@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .width(260.dp)
            .wrapContentHeight()
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column {

            // 🔹 Top Image (if you have event.image)
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(event.imageUrl?.takeIf { it.isNotBlank() })
                    .crossfade(true)
                    .placeholder(R.drawable.ic_holi)      // While loading
                    .error(R.drawable.ic_holi)        // Broken URL
                    .fallback(R.drawable.ic_holi)       // Null / Blank URL
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(14.dp)
            ) {

                // 🔹 Title
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 🔹 Date
                Text(
                    text = event.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4A4F5A)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 🔹 Location Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF7A8194),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = event.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7A8194)
                    )
                }
            }
        }
    }
}
