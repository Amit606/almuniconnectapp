package com.kwh.almuniconnect.almunipost

data class AlumniPost(
    val id: String,
    val name: String,
    val batch: String,
    val message: String
)
val alumniFeed = listOf(
    AlumniPost("1", "Amit Gupta", "MCA 2015", "Excited to attend Alumni Meet 2026! 🎉"),
    AlumniPost("2", "Rohit Sharma", "MCA 2018", "Great memories with HBTU MCA family ❤️"),
    AlumniPost("3", "Neha Verma", "MCA 2020", "Looking forward to networking with seniors!")
)
