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
    EmergencyDemo(
        id = "1",
        name = "Amit Gupta",
        batch = "2014",
        department = "MCA",
        title = "Medical Emergency",
        description = "Urgent funds required for heart surgery scheduled this week.",
        raised = 60000,
        target = 100000,
        verified = true
    ),

    // 2️⃣ Job Loss
    EmergencyDemo(
        id = "2",
        name = "Ravi Verma",
        batch = "2012",
        department = "CSE",
        title = "Job Loss Support",
        description = "Lost job due to company layoffs. Seeking alumni referrals and short-term financial help.",
        raised = 25000,
        target = 70000,
        verified = true
    ),

    // 3️⃣ Accident
    EmergencyDemo(
        id = "3",
        name = "Neha Sharma",
        batch = "2018",
        department = "ECE",
        title = "Accident Recovery",
        description = "Accident recovery support needed for surgery and medical expenses.",
        raised = 35000,
        target = 80000,
        verified = true
    ),

    // 4️⃣ Financial
    EmergencyDemo(
        id = "4",
        name = "Sandeep Kumar",
        batch = "2010",
        department = "ME",
        title = "Financial Crisis",
        description = "Facing severe financial hardship due to business loss. Immediate support required.",
        raised = 30000,
        target = 90000,
        verified = true
    ),

    // 5️⃣ Job Re-Entry
    EmergencyDemo(
        id = "5",
        name = "Pooja Singh",
        batch = "2016",
        department = "MBA",
        title = "Career Re-Entry Support",
        description = "Looking for job opportunities after maternity break and career gap.",
        raised = 15000,
        target = 50000,
        verified = true
    ),

    // 6️⃣ Medical (Child)
    EmergencyDemo(
        id = "6",
        name = "Ankit Mishra",
        batch = "2015",
        department = "IT",
        title = "Child Medical Treatment",
        description = "Funds required for child’s critical medical treatment and hospitalization.",
        raised = 42000,
        target = 100000,
        verified = true
    ),

    // 7️⃣ Job – Fresher
    EmergencyDemo(
        id = "7",
        name = "Kavita Joshi",
        batch = "2020",
        department = "CS",
        title = "Fresher Job Assistance",
        description = "Unable to secure first job after graduation. Needs referrals and guidance.",
        raised = 8000,
        target = 30000,
        verified = true
    ),

    // 8️⃣ Accident
    EmergencyDemo(
        id = "8",
        name = "Rohit Yadav",
        batch = "2013",
        department = "Civil",
        title = "Road Accident Support",
        description = "Injured in a road accident. Requires financial assistance for recovery.",
        raised = 27000,
        target = 65000,
        verified = true
    ),

    // 9️⃣ Overseas Job Loss
    EmergencyDemo(
        id = "9",
        name = "Vikas Chandra",
        batch = "2011",
        department = "Electrical",
        title = "Overseas Job Loss",
        description = "Lost overseas job due to visa issues. Seeking urgent job placement in India.",
        raised = 28000,
        target = 75000,
        verified = true
    ),

    // 🔟 Education / Financial
    EmergencyDemo(
        id = "10",
        name = "Sneha Patel",
        batch = "2019",
        department = "BBA",
        title = "Education Loan Support",
        description = "Struggling to repay education loan after job loss. Immediate help required.",
        raised = 19000,
        target = 55000,
        verified = true
    )
)

