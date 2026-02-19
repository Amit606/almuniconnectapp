package com.kwh.almuniconnect.almunipost

import com.kwh.almuniconnect.R


val alumniFeed = listOf(
    AlumniStory(
        name = "Pramod Rajput",
        batch = "B.Tech, Elect Engg 1985",
        title = "Senior Technical Director, at National Informatics Centre",
        companyOrStartup = "NIC",
        category = StoryCategory.SUCCESS,
        story = "From late-night coding sessions in college to leading global engineering teams, Rohan’s journey reflects perseverance, curiosity, and the power of alumni connections.",
        imageRes = R.drawable.man,
        featured = true
    ),

    AlumniStory(
        name = "Radhe Shyam Saini",
        batch = "MCA 2014",
        title = "Founder & CEO",
        companyOrStartup = "Neroworx",
        category = StoryCategory.STARTUP,
        story = "What began as a final-year project is now a fast-growing fintech startup serving over a million users across India.",
        imageRes = R.drawable.man
    ),



)
data class AlumniStory(
    val name: String? = null,
    val title: String? = null,
    val companyOrStartup: String? = null,
    val batch: String? = null,
    val story: String? = null,
    val imageRes: Int = 0,
    val featured: Boolean? = false,
    val category: StoryCategory? = null
)

enum class StoryCategory {
    SUCCESS,
    STARTUP,
    AWARD,
    FEATURED
}