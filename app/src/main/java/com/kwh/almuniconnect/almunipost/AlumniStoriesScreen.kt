package com.kwh.almuniconnect.almunipost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.TrackScreen

@Composable
fun AlumniStoriesScreen(
    navController: NavController,

) {
    TrackScreen("alumni_stories_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Stories and Achievements",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxWidth()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {


            LazyColumn {
                items(alumniFeed) { story ->
                    AlumniStoryCard(story = story, onClick = {
                        navController.navigate("story_detail/${story.name}")
                    } )
                }
            }
        }
    }
}

