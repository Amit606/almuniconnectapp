package com.kwh.almuniconnect.almunipost

import com.kwh.almuniconnect.R


val alumniFeed = listOf(
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

)
data class AlumniStory(
    val name: String,
    val batch: String,
    val title: String,
    val companyOrStartup: String,
    val category: StoryCategory,
    val story: String,
    val imageRes: Int,
    val featured: Boolean = false
)

enum class StoryCategory {
    SUCCESS,
    STARTUP,
    AWARD,
    FEATURED
}