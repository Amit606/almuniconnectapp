package com.kwh.almuniconnect.profile

data class Branch(
    val id: Int,
    val name: String,
    val shortName: String
)
val branches = listOf(
    Branch(1, "MCA", "AF"),
    Branch(2, "B.Tech – CSE", "CS"),
    Branch(3, "B.Tech – IT", "IT"),
    Branch(4, "B.Tech – ECE", "ECE"),
    Branch(5, "B.Tech – EE", "EE"),
    Branch(6, "B.Tech – ME", "ME"),
    Branch(7, "B.Tech – CE", "CE"),
    Branch(8, "M.Tech", "MT"),
    Branch(9, "MBA", "MBA"),
    Branch(10, "BCA", "BCA")
)
