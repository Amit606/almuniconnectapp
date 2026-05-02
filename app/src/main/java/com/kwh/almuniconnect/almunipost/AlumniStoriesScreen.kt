package com.kwh.almuniconnect.almunipost

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.analytics.TrackScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.model.AlumniStory

@Composable
fun AlumniStoriesScreen(
    navController: NavController,
    viewModel: SuccessViewModel = viewModel()

) {
    TrackScreen("alumni_stories_screen")
    val alumniList by viewModel.alumniList.collectAsState()

    Scaffold(
        containerColor = Color(0xFFF5F7FA),
        topBar = {
            HBTUTopBar(
                title = "Alumni Stories & Achievements",
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
            items(alumniList) { story ->
                AlumniPost(
                    post = story,
                    onClick = {
                        navController.navigate("story_detail/${story.name}")
                    }
                )
            }
        }
    }
}
@Composable
fun AlumniPost(
    post: AlumniStory,
    onClick: () -> Unit
) {

    val drawableId = getDrawableId(post.image)

    val fallback = R.drawable.man

    Card(
        modifier = Modifier

            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Image Loader
            if (post.image.startsWith("http")) {

                AsyncImage(
                    model = post.image,
                    contentDescription = "avatar",
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(fallback),
                    error = painterResource(fallback),
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

            } else {

                val imageRes =
                    if (drawableId != 0) drawableId else fallback

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = post.name,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = post.title,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = post.companyOrStartup,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Composable
fun AlumniStoryCard(
    story: AlumniStory,
    onClick: () -> Unit
) {

    val imageModel = when {
        story.image.startsWith("http") -> story.image
        else -> getDrawableId(story.image)
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            // 🔥 Blurred Background
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageModel)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .blur(20.dp),
                error = painterResource(R.drawable.man),
                placeholder = painterResource(R.drawable.man)
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(20.dp)
            ) {

                AsyncImage(
                    model = imageModel,
                    contentDescription = story.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape),
                    error = painterResource(R.drawable.man),
                    placeholder = painterResource(R.drawable.man)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = story.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = story.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = story.companyOrStartup,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )

                if (story.featured) {

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF0A66C2),
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Featured",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun getDrawableId(imageName: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName
    )
}