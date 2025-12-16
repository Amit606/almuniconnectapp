package com.kwh.almuniconnect.network

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkScreen(
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
            CenterAlignedTopAppBar(
                title = { Text("Almuni Network") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, tint = Color.White, contentDescription = "Back")
                    }
                }
              ,colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420),
                    titleContentColor = Color.White
                )
            )
        },
       // containerColor = Color(0xFF0E1420),

        ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0E1420))
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF142338)
        ),
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
                Text(alumni.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                Text("${alumni.branch} - Batch of ${alumni.passingYear}", color = Color.LightGray,fontSize = 14.sp)
                Text(alumni.company, style = MaterialTheme.typography.bodySmall,color = Color.Gray)
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF142338)
                ),

                onClick = { /* send connection */ }) {
                Text("Connect", color = Color.White)
            }
        }
    }
}

data class AlumniProfile(
    val id: String,
    val name: String,
    val branch: String,
    val passingYear: String,
    val company: String,
    val imageUrl: String
)

fun sampleAlumniProfiles(): List<AlumniProfile> = listOf(
    AlumniProfile("1", "Rohit Sharma", "CSE", "2022", "Google", "https://i.pravatar.cc/150?img=10"),
    AlumniProfile("2", "Priya Mehta", "ECE", "2023", "Microsoft", "https://i.pravatar.cc/150?img=12"),
    AlumniProfile("3", "Ankit Verma", "Mechanical", "2020", "Tata Motors", "https://i.pravatar.cc/150?img=8"),
    AlumniProfile("4", "Sneha Singh", "IT", "2021", "Amazon", "https://i.pravatar.cc/150?img=5"),
    AlumniProfile("5", "Vivek Agarwal", "Civil", "2019", "L&T", "https://i.pravatar.cc/150?img=14"),
)
