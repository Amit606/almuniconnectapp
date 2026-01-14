package com.kwh.almuniconnect.network

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(
    navController: NavController,
    onOpenProfile: (AlumniProfile) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedBranch by remember { mutableStateOf("All") }
    var selectedYear by remember { mutableStateOf("All") }

    val branches = listOf("All", "CSE", "ECE", "IT", "Mechanical", "Civil", "Electrical")
    val years = listOf("All", "2023", "2022", "2021", "2020", "2019", "2018")

    val allAlumni = remember {
        sampleAlumniProfiles()
    }

    // Filter logic
    val filteredList = allAlumni.filter {
        (selectedBranch == "All" || it.branch == selectedBranch) &&
        (selectedYear == "All" || it.passingYear == selectedYear) &&
        (searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Networks ",
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
              //  .background(Color(0xFF0E1420))
                .padding(horizontal = 16.dp)
        ) {
            // Search


            Spacer(modifier = Modifier.height(8.dp))

            // Filters
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    FilterChipDropdown(
                        label = "Branch",
                        options = branches,
                        selectedOption = selectedBranch,
                        onSelect = { selectedBranch = it }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    FilterChipDropdown(
                        label = "Year",
                        options = years,
                        selectedOption = selectedYear,
                        onSelect = { selectedYear = it }
                    )
                }
            }

            // Alumni List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList) { alumni ->
                    AlumniCard(alumni = alumni, onClick = { onOpenProfile(alumni) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
       // modifier = Modifier.weight(1f)
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = Color.White) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AlumniCard(alumni: AlumniProfile, onClick: () -> Unit) {
    val context = LocalContext.current
    Card(
        onClick = onClick, // ✅ ONLY THIS

        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = alumni.imageUrl,
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(alumni.name,  style = MaterialTheme.typography.titleMedium)
                Text("${alumni.branch} - Batch of ${alumni.passingYear}",  style = MaterialTheme.typography.titleMedium)
                Text(alumni.company, style = MaterialTheme.typography.bodySmall,color = Color.Gray)
            }

            Button(
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF142338)
//                ),

                onClick = { openUrl(context, alumni.profileUrl) }) {
                Text("Connect", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

fun openUrl(context: Context, url: String) {
    if (url.isBlank()) return

    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    context.startActivity(intent)
}


fun sampleAlumniProfiles(): List<AlumniProfile> = listOf(

    AlumniProfile(
        id = "1",
        name = "Amit Gupta",
        branch = "MCA",
        passingYear = "2015",
        company = "Datability Technology Pvt. Ltd.",
        position = "Senior Software Engineering  Developer",
        phone = "+91 98xxxxxxxx",
        email = "amit.xxxx@gmail.com",
        location = "Noida, India",
        imageUrl = "https://media.licdn.com/dms/image/v2/D5603AQHPW9sCI3l87Q/profile-displayphoto-shrink_800_800/B56ZPxrPKEH0Ac-/0/1734926465012?e=1769644800&v=beta&t=CLvdtkZ_-RMdZ5qlg9AHqWy3OaSAUaURUpogstIMP2Y",
        profileUrl = "https://www.linkedin.com/in/amitguptaandroid/"
    ),

    AlumniProfile(
        id = "2",
        name = "Ravi Prakash",
        branch = "MCA",
        passingYear = "2013",
        company = "Microsoft",
        position = "Software Engineer",
        phone = "+91 9876501234",
        email = "ravi.prakash@gmail.com",
        location = "Hyderabad, India",
        imageUrl = "https://i.pravatar.cc/150?img=12",
        profileUrl = "https://www.linkedin.com/in/ravi-prakash"
    ),

    AlumniProfile(
        id = "3",
        name = "Neha Sharma",
        branch = "MCA",
        passingYear = "2016",
        company = "Infosys",
        position = "Technology Analyst",
        phone = "+91 9876512345",
        email = "neha.sharma@gmail.com",
        location = "Pune, India",
        imageUrl = "https://i.pravatar.cc/150?img=5",
        profileUrl = "https://www.linkedin.com/in/neha-sharma"
    ),

    AlumniProfile(
        id = "4",
        name = "Ankit Verma",
        branch = "MCA",
        passingYear = "2014",
        company = "TCS",
        position = "System Engineer",
        phone = "+91 9876523456",
        email = "ankit.verma@gmail.com",
        location = "Delhi, India",
        imageUrl = "https://i.pravatar.cc/150?img=8",
        profileUrl = "https://www.linkedin.com/in/ankit-verma"
    ),

    AlumniProfile(
        id = "5",
        name = "Pooja Singh",
        branch = "MCA",
        passingYear = "2017",
        company = "Wipro",
        position = "Project Engineer",
        phone = "+91 9876534567",
        email = "pooja.singh@gmail.com",
        location = "Bangalore, India",
        imageUrl = "https://i.pravatar.cc/150?img=16",
        profileUrl = "https://www.linkedin.com/in/pooja-singh"
    ),

    AlumniProfile(
        id = "6",
        name = "Saurabh Mishra",
        branch = "MCA",
        passingYear = "2012",
        company = "Accenture",
        position = "Application Development Lead",
        phone = "+91 9876545678",
        email = "saurabh.mishra@gmail.com",
        location = "Gurgaon, India",
        imageUrl = "https://i.pravatar.cc/150?img=14",
        profileUrl = "https://www.linkedin.com/in/saurabh-mishra"
    ),

    AlumniProfile(
        id = "7",
        name = "Kunal Jain",
        branch = "MCA",
        passingYear = "2018",
        company = "Paytm",
        position = "Android Developer",
        phone = "+91 9876556789",
        email = "kunal.jain@gmail.com",
        location = "Noida, India",
        imageUrl = "https://i.pravatar.cc/150?img=20",
        profileUrl = "https://www.linkedin.com/in/kunal-jain"
    ),

    AlumniProfile(
        id = "8",
        name = "Shreya Kapoor",
        branch = "MCA",
        passingYear = "2019",
        company = "Amazon",
        position = "SDE I",
        phone = "+91 9876567890",
        email = "shreya.kapoor@gmail.com",
        location = "Bangalore, India",
        imageUrl = "https://i.pravatar.cc/150?img=25",
        profileUrl = "https://www.linkedin.com/in/shreya-kapoor"
    ),

    AlumniProfile(
        id = "9",
        name = "Rahul Yadav",
        branch = "MCA",
        passingYear = "2011",
        company = "IBM",
        position = "Technical Consultant",
        phone = "+91 9876578901",
        email = "rahul.yadav@gmail.com",
        location = "Chennai, India",
        imageUrl = "https://i.pravatar.cc/150?img=18",
        profileUrl = "https://www.linkedin.com/in/rahul-yadav"
    ),

    AlumniProfile(
        id = "10",
        name = "Simran Kaur",
        branch = "MCA",
        passingYear = "2020",
        company = "Startup Founder",
        position = "Product Manager",
        phone = "+91 9876589012",
        email = "simran.kaur@gmail.com",
        location = "Delhi, India",
        imageUrl = "https://i.pravatar.cc/150?img=30",
        profileUrl = "https://www.linkedin.com/in/simran-kaur"
    )
)

data class AlumniProfile(
    val id: String,
    val name: String,
    val branch: String,
    val passingYear: String,
    val company: String,
    val position: String,          // ✅ Job position
    val phone: String,             // ✅ Mobile number
    val email: String,             // ✅ Email
    val location: String,          // ✅ City / Location
    val imageUrl: String,
    val profileUrl: String
)
