package com.kwh.almuniconnect.almunipost

import com.kwh.almuniconnect.R


val alumniFeed = listOf(
    AlumniStory(
        name = "Pramod Rajput",
        batch = "B.Tech, Electrical Engineering 1985",
        title = "Senior Technical Director, National Informatics Centre",
        companyOrStartup = "National Informatics Centre (NIC)",
        category = StoryCategory.SUCCESS,
        story = "After graduating in engineering, Pramod Rajput advanced into leadership in government IT, ultimately becoming Senior Technical Director at the National Informatics Centre (NIC). There he has led key technology initiatives supporting Digital India and government digital services, reflecting a commitment to public service and innovation in technology.",
        imageRes = R.drawable.ic_pk_rajput,
        featured = true
    ),

    AlumniStory(
        name = "Sanjeev Batra",
        batch = "B.Tech / MCA (est.)",
        title = "Technology & Business Transformation Leader",
        companyOrStartup = "Servoedge Technologies / SANMIT Technologies",
        category = StoryCategory.SUCCESS,
        story = "Sanjeev Batra is a seasoned technology and business transformation leader with over two decades of experience driving enterprise automation, AI adoption, and digital transformation across global organizations. Throughout his career, he has led strategic IT initiatives that blend innovation with operational excellence, helping companies scale and adapt to the evolving tech landscape. His journey reflects leadership, technical expertise, and a commitment to empowering businesses through technology.",
        imageRes = R.drawable.ic_sanjeev,
        featured = true
    ),
    AlumniStory(
        name = "Atul Singhal",
        batch = "B.Tech / MCA (est.)",
        title = "Senior IT Professional",
        companyOrStartup = "Kloudrac Group",
        category = StoryCategory.SUCCESS,
        story = "Atul Singhal is a seasoned tech leader with over 17 years of experience in SaaS and cloud-based solutions. At Kloudrac Group, he has played a key role in building scalable platforms and driving digital transformation. His career reflects a deep commitment to innovation and building robust tech products that serve businesses across industries.",
        imageRes = R.drawable.ic_atul,
        featured = true
    ),

    AlumniStory(
        name = "Radhe Shyam Saini",
        batch = "MCA 2014",
        title = "Founder & CEO",
        companyOrStartup = "Neroworx",
        category = StoryCategory.STARTUP,
        story = "Radhe Shyam Saini turned his final-year MCA project into a thriving fintech startup, Neroworx, focused on innovative financial solutions for users across India. Since launching, the company has scaled rapidly and built a loyal user base by solving real-world financial challenges with technology and creativity.",
        imageRes = R.drawable.ic_radhe,
        featured = true
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