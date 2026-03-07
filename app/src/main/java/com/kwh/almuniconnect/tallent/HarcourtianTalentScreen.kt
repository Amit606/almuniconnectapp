package com.kwh.almuniconnect.tallent

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarcourtianTalentScreen(
    navController: NavController,
) {

    val viewModel: TalentViewModel = viewModel()
    val talents by viewModel.talents.collectAsState()
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
// 🔥 Filter Logic
    val filteredTalents = remember(talents, selectedCategory) {
        if (selectedCategory == "All") {
            talents
        } else {
            talents.filter {
                it.skill.contains(selectedCategory, ignoreCase = true)
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Harcourtian Talent",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.ADD_TALENT_LIST) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Talent")
            }
        }
    ) { paddingValues ->


        if (talents.isEmpty()) {

            // -------- EMPTY STATE --------
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "No Talents Yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Be the first to showcase your talent!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

        } else {
            // 🔹 Filter Chips
            TalentFilterSection(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // -------- TALENT LIST --------
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = talents,
                    key = { it.id }
                ) { talent ->

                    val isYoutube = talent.videoLink.contains("youtube.com") ||
                            talent.videoLink.contains("youtu.be")

                    TalentYoutubeStyleCard(
                        talent = talent,
                        onClick = {

                            if (isYoutube) {

                                val intent = Intent(Intent.ACTION_VIEW, talent.videoLink.toUri())
                                intent.setPackage("com.google.android.youtube")

                                try {
                                    context.startActivity(intent)
                                } catch (e: ActivityNotFoundException) {
                                    context.startActivity(
                                        Intent(Intent.ACTION_VIEW, talent.videoLink.toUri())
                                    )
                                }

                            } else {

                                val intent = Intent(Intent.ACTION_VIEW, talent.videoLink.toUri())
                                intent.setPackage("com.android.chrome")

                                try {
                                    context.startActivity(intent)
                                } catch (e: ActivityNotFoundException) {
                                    context.startActivity(
                                        Intent(Intent.ACTION_VIEW, talent.videoLink.toUri())
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

fun shareTalent(context: Context, talent: Talent) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out ${talent.name}'s amazing talent!\n${talent.videoLink}"
        )
    }
    context.startActivity(Intent.createChooser(intent, "Share via"))
}
@Composable
fun TalentYoutubeStyleCard(
    talent: Talent,
    onClick: () -> Unit = {}
) {

    val thumbnailUrl = remember(talent.videoLink) {
        getYoutubeThumbnail(talent.videoLink)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            // ---------- THUMBNAIL ----------
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(90.dp) // 16:9 ratio
            ) {

                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.ic_second),
                    placeholder = painterResource(R.drawable.ic_second),
                    fallback = painterResource(R.drawable.ic_second)
                )


                // Duration bottom-right
                Text(
                    text = "${talent.skill ?: ""} ${getSkillIcon(talent.skill ?: "")}",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // ---------- RIGHT CONTENT ----------
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.Top
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "${talent.name ?: ""} |  ${talent.branch ?: ""}|  ${talent.year ?: ""}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = talent.description ?: "New Digital Music",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )


                    }


                }
            }
        }
    }
}
@Composable
fun getSkillIcon(skill: String): String {
    return when (skill.lowercase()) {
        "singer" -> "🎤"
        "musician" -> "🎼"
        "dancer" -> "💃"
        "educator" -> "📚"
        "poetry" -> "✍️"
        "writer" -> "🖊"
        "public speaker" -> "🎙"
        "developer" -> "💻"
        "designer" -> "🎨"
        "entrepreneur" -> "🚀"
        "sports" -> "🏏"
        else -> "✨"
    }
}
fun getYoutubeThumbnail(url: String): String {
    val videoId = url.substringAfter("v=").substringBefore("&")
    return "https://img.youtube.com/vi/$videoId/0.jpg"
}
