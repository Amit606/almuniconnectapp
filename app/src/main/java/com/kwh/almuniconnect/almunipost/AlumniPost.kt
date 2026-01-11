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
    AlumniPost("3", "Neha Verma", "MCA 2020", "Looking forward to networking with seniors!"),

    AlumniPost("4", "Sandeep Kumar", "MCA 2012", "HBTU gave me lifelong friends and amazing career opportunities 🙏"),
    AlumniPost("5", "Priya Singh", "MCA 2016", "Can't wait to reconnect with batchmates at Alumni Meet 2026 😊"),
    AlumniPost("6", "Ankit Mishra", "MCA 2014", "Alumni gatherings always refresh old golden days ✨"),
    AlumniPost("7", "Pooja Agarwal", "MCA 2019", "Proud to be a part of HBTU MCA family 🎓"),
    AlumniPost("8", "Vikas Yadav", "MCA 2013", "Great initiative to bring all MCA alumni together 🤝"),
    AlumniPost("9", "Ritu Gupta", "MCA 2021", "Hope to learn a lot from seniors during this event 🌟"),
    AlumniPost("10", "Manish Tiwari", "MCA 2010", "HBTU memories are forever ❤️ Looking forward to meeting everyone!")

)
