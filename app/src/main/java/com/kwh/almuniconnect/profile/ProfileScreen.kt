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
import androidx.compose.runtime.remember
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
import com.kwh.almuniconnect.network.AlumniProfile
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }

    val user by userPrefs.getUser().collectAsState(
        initial = UserLocalModel("", "", "", "")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // 🔹 HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hbtu),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000))
            )

            Text(
                text = "User PROFILE",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )
        }

        // 🔹 PROFILE IMAGE (OVERLAP)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-60).dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (user.photo.isNotEmpty()) {
                AsyncImage(
                    model = user.photo,
                    contentDescription = "Profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.man),
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 NAME
        Text(
            text = user.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "India",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 SCROLLABLE DETAILS
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.verticalScroll(rememberScrollState())
        ) {
            ProfileDetailsSection(
                name = user.name,
                email = user.email
            )
        }
        // ✏️ EDIT PROFILE FAB
        FloatingActionButton(
            onClick = {
                navController.navigate("edit_profile")
            },
            containerColor = Color(0xFF8E44AD),
            contentColor = Color.White,

        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile"
            )
        }
    }
}

@Composable
fun ProfileDetailsSection(name: String,email:String) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {  ProfileItem("Name",name)}
        item { ProfileItem("Email", email) }
        item {  ProfileItem("Mobile No", "7905717240") }
        item {  ProfileItem("Branch & Year", " MCA 2015") }
        item {  ProfileItem("Job", "IT Engineer (iZooto)") }
        item { ProfileItem("Location", "Noida") }
        item { ProfileItem("Birthday", "24 April 1991") }
        item { ProfileItem("JobPost", "1")}
        item { ProfileItem("Linkied ID", "https://aaaaa") }
    }
}
@Composable
fun ProfileItem(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color(0xFF8E44AD),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 4.dp)
        )
        Divider(modifier = Modifier.padding(top = 12.dp))
    }
}
