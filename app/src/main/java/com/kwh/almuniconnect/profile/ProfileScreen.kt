package com.kwh.almuniconnect.profile
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.jobposting.AppTextField
import com.kwh.almuniconnect.network.AlumniProfile
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(
        initial = UserLocalModel("", "", "", "")
    )

    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var branch by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var mobile by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }

    val branches = listOf(
        "B.Tech – Computer Science & Engineering",
        "B.Tech – Information Technology",
        "B.Tech – Electronics & Communication",
        "B.Tech – Electrical Engineering",
        "B.Tech – Mechanical Engineering",
        "B.Tech – Civil Engineering",
        "B.Tech – Chemical Engineering",
        "B.Tech – Bio Technology",
        "B.Tech – Industrial & Production Engineering",
        "M.Tech – Computer Science",
        "M.Tech – Electrical Engineering",
        "M.Tech – Mechanical Engineering",
        "M.Tech – Civil Engineering",
        "MCA – Master of Computer Applications",
        "MBA – Master of Business Administration",
        "BCA – Bachelor of Computer Applications"
    )
    val years = (1972..2026).map { it.toString() }


    Scaffold(
        containerColor = Color(0xFFF5F6FA),
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        contentColor = Color(0xFF0E1420)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF0E1420))
        ) {

            // 🔥 Header with image
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.hbtu),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color(0x88000000))
                    )
                }
            }

            // 🔥 Profile picture
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-50).dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = user.photo.ifEmpty { R.drawable.man },
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                Text(
                    user.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "India",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = Color.Gray
                )
            }

            item { Spacer(Modifier.height(20.dp)) }

            // 🔥 Form
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF142338)
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {

                        AppTextField("Name", name) { name = it }
                        AppTextField("Email", email) { email = it }
                        AppTextField("Mobile", mobile) { mobile = it }

                        Spacer(Modifier.height(10.dp))
                        DropdownField("Branch", branch, branches) { branch = it }

                        Spacer(Modifier.height(10.dp))
                        DropdownField("Year", year, years) { year = it }


                        AppTextField("Job / Company", job) { job = it }
                        AppTextField("Location", location) { location = it }
                        AppTextField("Birthday", birthday) { birthday = it }
                        AppTextField("LinkedIn URL", linkedin) { linkedin = it }

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = {
                                // Save to DataStore / Firebase here
                                navController.navigate("home") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A5AE0))
                        ) {
                            Text("Update Profile", color = Color.White)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(50.dp)) }
        }
    }
}

