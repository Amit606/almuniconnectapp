package com.kwh.almuniconnect.branding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.compose.foundation.lazy.items


@Composable
fun ProductServiceDummyScreen(
    navController: NavController
) {
    val context = LocalContext.current

    TrackScreen("product_details_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Service Details",
                navController = navController
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {



        }
    }
}
@Composable
fun ProductServiceCard(
    item: ProductServiceItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {

            // 🖼️ Image (Alumni photo / fallback)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.photoUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.ic_services)
                    .error(R.drawable.ic_services)
                    .fallback(R.drawable.ic_services)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {

                TypeChip(item.type)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(12.dp))

                AlumniFooter(item)
            }
        }
    }
}
@Composable
fun TypeChip(type: String) {
    val bgColor = if (type.equals("Product", true))
        Color(0xFFE3F2FD)
    else
        Color(0xFFE8F5E9)

    val textColor = if (type.equals("Product", true))
        Color(0xFF1565C0)
    else
        Color(0xFF2E7D32)

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = type,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}
@Composable
fun AlumniFooter(item: ProductServiceItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.alumniName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${item.courseName} • Batch ${item.batch}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

//        Text(
//            text = "View",
//            style = MaterialTheme.typography.labelLarge,
//            color = MaterialTheme.colorScheme.primary,
//            fontWeight = FontWeight.Bold
//        )
    }
}


