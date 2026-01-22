package com.kwh.almuniconnect.emergency

data class EmergencyDemo(
    val id: String,
    val name: String,
    val batch: String,
    val department: String,
    val title: String,
    val description: String,
    val raised: Int,
    val target: Int,
    val verified: Boolean
)

val demoEmergencyList = listOf(
    EmergencyDemo(
        id = "1",
        name = "Amit Gupta",
        batch = "2014",
        department = "MCA",
        title = "Medical Emergency",
        description = "Urgent medical funds required for heart surgery scheduled this week.",
        raised = 60000,
        target = 100000,
        verified = true
    ),
    EmergencyDemo(
        id = "2",
        name = "Ravi Verma",
        batch = "2012",
        department = "CSE",
        title = "Accident Recovery",
        description = "Accident recovery support needed for medical expenses.",
        raised = 35000,
        target = 80000,
        verified = true
    ),
    EmergencyDemo(
        id = "3",
        name = "Neha Sharma",
        batch = "2018",
        department = "ECE",
        title = "Financial Emergency",
        description = "Financial aid required due to sudden job loss.",
        raised = 15000,
        target = 50000,
        verified = true
    )
)
