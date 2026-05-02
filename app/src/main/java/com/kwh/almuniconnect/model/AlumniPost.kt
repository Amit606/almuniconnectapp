package com.kwh.almuniconnect.model

data class AlumniStory(
    val name: String = "",
    val title: String = "",
    val companyOrStartup: String = "",
    val batch: String = "",
    val story: String = "",
    val image: String = "",          // 👈 change from imageRes Int
    val featured: Boolean = false,
    val linkedURl : String = "",
    val category: StoryCategory = StoryCategory.SUCCESS
)

enum class StoryCategory {
    SUCCESS,
    STARTUP,
    AWARD,
    FEATURED
}