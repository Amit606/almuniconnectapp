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

    // 1️⃣ Medical


    // 2️⃣ Job Loss
    EmergencyDemo(
        id = "1",
        name = "Ravindra Mohan",
        batch = "2002",
        department = "MCA",
        title = "Kidney Problem - Urgent Help Needed",
        description = "Diagnosed with severe kidney issues. Need funds for treatment and surgery.",
        raised = 12000,
        target = 100000,
        verified = true

    ),


)

