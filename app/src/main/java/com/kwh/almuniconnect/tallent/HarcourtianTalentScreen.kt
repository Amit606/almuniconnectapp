package com.kwh.almuniconnect.tallent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarcourtianTalentScreen(
    navController: NavController,
) {

    val viewModel: TalentViewModel = viewModel()
    val talents by viewModel.talents.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Harcourtian Talent",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_talent") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Talent")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {



            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Featured Talents",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(talents) { talent ->
                    TalentCard(
                        talent = talent,
                        onLikeClick = {
                            viewModel.likeTalent(talent.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(title: String) {
    AssistChip(
        onClick = { },
        label = { Text(title) },
        modifier = Modifier.padding(end = 8.dp)
    )
}

@Composable
fun TalentCard(
    talent: Talent,
    onLikeClick: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = talent.name.first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(
                            text = talent.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        if (talent.rating >= 4.8) {
                            Spacer(modifier = Modifier.width(6.dp))
                            TopRatedBadge()
                        }
                    }

                    Text(
                        text = talent.skill,
                        color = Color.Gray
                    )

                    Text(
                        text = "${talent.year} • ${talent.branch}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Like",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⭐ ${talent.rating}",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Hide Showcase" else "View Showcase")
            }

            if (expanded) {
                YoutubePlayerView(talent.videoLink)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "❤️ ${talent.likes} Appreciations",
                fontSize = 12.sp
            )
        }
    }
}
@Composable
fun TopRatedBadge() {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFFFD700)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🏆 Top Rated", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
