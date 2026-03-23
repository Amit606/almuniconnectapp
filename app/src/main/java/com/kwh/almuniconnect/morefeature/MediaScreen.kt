package com.kwh.almuniconnect.morefeature
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

data class MediaItem(
    val url: String = "",
    val type: String = "",
    val title: String = ""
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(navController: NavController) {

    var mediaList by remember { mutableStateOf<List<MediaItem>>(emptyList()) }


    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        fetchMediaList {
            mediaList = it
        }
    }

    val filteredList = if (selectedTab == 0) {
        mediaList.filter { it.type == "photo" }
    } else {
        mediaList.filter { it.type == "video" }
    }
    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Media 🎥",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Photos") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Videos") }
                )
            }


            if (mediaList.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(filteredList) { item ->
                        StaggeredMediaItem(item)
                    }
                }
            }
        }

            // Grid


    }
}
@Composable
fun StaggeredMediaItem(item: MediaItem) {

    val randomHeight = listOf(140.dp, 180.dp, 220.dp).random()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(randomHeight)
            .clip(RoundedCornerShape(16.dp))
            .clickable { }
    ) {

        AsyncImage(
            model = item.url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🔥 Gradient Top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(0.7f),
                            Color.Transparent
                        )
                    )
                )
        )

        // 📝 Title
        Text(
            text = item.title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        )

        // ▶️ Video icon
        if (item.type == "video") {
            YoutubeThumbnail(item.url)
//            VideoThumbnailItem(
//                thumbnailUrl = item.url,
//                duration = item.title
//            )
//            Icon(
//                Icons.Default.PlayCircle,
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier
//                    .size(32.dp)
//                    .align(Alignment.Center)
//            )
        }
    }
}
@Composable
fun YoutubeThumbnail(videoUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {

        AsyncImage(
            model = getYoutubeThumbnail(videoUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Icon(
            Icons.Default.PlayCircle,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
}
fun getYoutubeThumbnail(url: String): String {
    return try {
        val videoId = when {
            url.contains("youtu.be") ->
                url.substringAfter("youtu.be/").substringBefore("?")

            url.contains("watch?v=") ->
                url.substringAfter("v=").substringBefore("&")

            else -> ""
        }
        "https://img.youtube.com/vi/$videoId/0.jpg"
    } catch (e: Exception) {
        ""
    }
}
@Composable
fun YouTubePlayerScreen(videoId: String) {

    val context = LocalContext.current

    AndroidView(
        factory = {
            YouTubePlayerView(context).apply {

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(player: YouTubePlayer) {
                        player.loadVideo(videoId, 0f)
                    }
                })
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}

fun extractYoutubeId(url: String): String {
    return when {
        url.contains("youtu.be") ->
            url.substringAfter("youtu.be/").substringBefore("?")

        url.contains("v=") ->
            url.substringAfter("v=").substringBefore("&")

        else -> ""
    }
}
