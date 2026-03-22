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
            Icon(
                Icons.Default.PlayCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
            )
        }
    }
}