package com.kwh.almuniconnect.almunipost
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kwh.almuniconnect.analytics.TrackScreen

@Composable
fun AlumniStoryDetailScreen(
    navController : NavController,
    story: AlumniStory

) {
    TrackScreen("alumni_story_detail_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Stories and Achievements",
                navController = navController
            )
        }
    ) { paddingValues ->


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            /* Cover Image */
            item {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = story.imageRes),
                    contentDescription = "Alumni Cover",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {

                    if (story.featured) {
                        Text(
                            text = "⭐ Featured Alumni",
                            color = Color(0xFFB8860B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(
                        text = story.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${story.title} • ${story.companyOrStartup}",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "Batch of ${story.batch}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    CategoryChip(story.category)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Story",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = story.story,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Message to Juniors",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "“Stay curious, stay humble, and never forget your roots.”",
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
@Composable
fun CategoryChip(category: StoryCategory) {
    val (text, color) = when (category) {
        StoryCategory.SUCCESS -> "Success Story" to Color(0xFF4CAF50)
        StoryCategory.STARTUP -> "Startup" to Color(0xFF2196F3)
        StoryCategory.AWARD -> "Award & Recognition" to Color(0xFFFF9800)
        StoryCategory.FEATURED -> "Featured" to Color(0xFF9C27B0)
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
