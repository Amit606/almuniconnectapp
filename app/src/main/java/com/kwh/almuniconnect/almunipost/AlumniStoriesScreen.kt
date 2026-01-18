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
    val displayedStories = dummyAlumniStories
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
                items(displayedStories) { story ->
                    AlumniStoryCard(story = story, onClick = {
                        navController.navigate("story_detail/${story.name}")
                    } )
                }
            }
        }
    }
}

val dummyAlumniStories = listOf(

    AlumniStory(
        name = "Rohan Verma",
        batch = "2012",
        title = "Senior Engineering Manager",
        companyOrStartup = "Google",
        category = StoryCategory.SUCCESS,
        story = "From late-night coding sessions in college to leading global engineering teams, Rohan’s journey reflects perseverance, curiosity, and the power of alumni connections.",
        imageRes = R.drawable.man,
        featured = true
    ),

    AlumniStory(
        name = "Ananya Singh",
        batch = "2016",
        title = "Founder & CEO",
        companyOrStartup = "FinEdge",
        category = StoryCategory.STARTUP,
        story = "What began as a final-year project is now a fast-growing fintech startup serving over a million users across India.",
        imageRes = R.drawable.newggg
    ),

    AlumniStory(
        name = "Dr. Amit Kulkarni",
        batch = "2005",
        title = "Senior Research Scientist",
        companyOrStartup = "ISRO",
        category = StoryCategory.AWARD,
        story = "Recipient of a National Science Award, Dr. Amit has contributed to landmark space missions and continues to mentor young innovators.",
        imageRes = R.drawable.newggg
    ),

    AlumniStory(
        name = "Neha Gupta",
        batch = "2010",
        title = "Social Entrepreneur",
        companyOrStartup = "EducateIndia NGO",
        category = StoryCategory.FEATURED,
        story = "Leaving behind a corporate career, Neha chose to transform rural education and has positively impacted thousands of students.",
        imageRes = R.drawable.first,
        featured = true
    ),

    AlumniStory(
        name = "Saurabh Mishra",
        batch = "2018",
        title = "Product Manager",
        companyOrStartup = "Microsoft",
        category = StoryCategory.SUCCESS,
        story = "Saurabh credits his success to strong fundamentals and guidance from seniors, proving that alumni support shapes future leaders.",
        imageRes = R.drawable.second
    )
)