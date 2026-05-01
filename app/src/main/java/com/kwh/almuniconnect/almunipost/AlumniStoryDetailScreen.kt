package com.kwh.almuniconnect.almunipost

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.model.AlumniStory
import com.kwh.almuniconnect.model.StoryCategory

@Composable
fun AlumniStoryDetailScreen(
    navController: NavController,
    story: AlumniStory?
) {

    TrackScreen("alumni_story_detail_screen")
    Log.e("Name", "Received name: ${story?.name}")
  val context = LocalContext.current
    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Stories and Achievements",
                navController = navController
            )
        }
    ) { paddingValues ->

        // ✅ NULL STORY SAFE UI
        if (story == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Story not available",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ✅ Safe Image Handling
            item {

                SafeImage(
                    image = story.image,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Featured Badge
                    if (story.featured == true) {
                        Text(
                            text = "⭐ Featured Alumni",
                            color = Color(0xFFB8860B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Name
                    Text(
                        text = story.name ?: "Unknown Alumni",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Title + Company
                    Text(
                        text = "${story.title ?: "N/A"} • ${story.companyOrStartup ?: "N/A"}",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )

                    // Batch
                    Text(
                        text = "Batch of ${story.batch ?: "N/A"}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Category Chip Safe
                    CategoryChip(story.category)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Story",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = story.story ?: "No story available.",
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))
                    story.linkedURl?.takeIf { it.isNotBlank() }?.let { url ->

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "View LinkedIn Profile",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
@Composable
fun CategoryChip(category: StoryCategory?) {

    val (text, color) = when (category) {
        StoryCategory.SUCCESS -> "Success Story" to Color(0xFF4CAF50)
        StoryCategory.STARTUP -> "Startup" to Color(0xFF2196F3)
        StoryCategory.AWARD -> "Award & Recognition" to Color(0xFFFF9800)
        StoryCategory.FEATURED -> "Featured" to Color(0xFF9C27B0)
        null -> "General" to Color.Gray
    }

    Box(
        modifier = Modifier
            .background(
                color.copy(alpha = 0.15f),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SafeImage(
    image: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val drawableId = if (!image.isNullOrEmpty()) getDrawableId(image) else 0

    val model: Any =
        when {
            image.isNullOrBlank() -> R.drawable.man
            image.startsWith("http") -> image
            drawableId != 0 -> drawableId
            else -> R.drawable.man
        }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(model)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds, // 🔥 FIXED
        placeholder = painterResource(R.drawable.man),
        error = painterResource(R.drawable.man),
        modifier = modifier
    )
}

//@Composable
//fun SafeImage(
//    image: String?,
//    modifier: Modifier = Modifier
//) {
//
//    val context = LocalContext.current
//
//    val drawableId = if (!image.isNullOrEmpty()) getDrawableId(image) else 0
//
//    val model: Any =
//        when {
//            image.isNullOrBlank() -> R.drawable.man
//            image.startsWith("http") -> image
//            drawableId != 0 -> drawableId
//            else -> R.drawable.man
//        }
//
//    AsyncImage(
//        model = ImageRequest.Builder(context)
//            .data(model)
//            .crossfade(true)
//            .build(),
//        contentDescription = null,
//        contentScale = ContentScale.Crop,
//        placeholder = painterResource(R.drawable.man),
//        error = painterResource(R.drawable.man),
//        modifier = modifier
//    )
//}