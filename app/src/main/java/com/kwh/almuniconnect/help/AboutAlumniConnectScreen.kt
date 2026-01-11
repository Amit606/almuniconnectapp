package com.kwh.almuniconnect.help

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes.APP_VERSION
import com.kwh.almuniconnect.Routes.DEVELOPER_EMAIL
import com.kwh.almuniconnect.Routes.DEVELOPER_NAME
import com.kwh.almuniconnect.Routes.PRIVACY_POLICY_URL
import com.kwh.almuniconnect.Routes.TERMS_URL

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun AboutAlumniConnectScreen(navController: NavController) {

    Scaffold(
        containerColor = Color(0xFF0E1420),
        topBar = {
            TopAppBar(
                title = { Text("About AlumniConnect") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0E1420))
                .padding(padding)
        ) {

            item { AboutHeader() }
            item { AboutSection("🎓 What is AlumniConnect?", aboutText1) }
            item { AboutSection("🤝 Our Mission", aboutText2) }
            item { AboutSection("🚀 Why AlumniConnect?", aboutText3) }
            item { AboutSection("📱 App Features", aboutText4) }
            item { AboutSection("💙 Built for HBTU Alumni", aboutText5) }
            item { AboutFooter() }
        }
    }
}
@Composable
fun AboutHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF6A5AE0), modifier = Modifier.size(64.dp))
        Text("AlumniConnect", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Connecting Alumni. Creating Opportunities.", color = Color.Gray)
    }
}
val aboutText1 =
    "AlumniConnect is a digital platform designed to bring college alumni, students, and faculty together in one place. It helps alumni stay connected with their institution and with each other."

val aboutText2 =
    "Our mission is to strengthen alumni relationships, provide mentorship opportunities, and support career growth through networking, events, and job sharing."

val aboutText3 =
    "Unlike social media, AlumniConnect is focused only on college connections. It provides a trusted and private space for alumni to communicate, collaborate, and grow."

val aboutText4 =
    "You can join alumni events, post updates, find jobs, chat with seniors, and stay informed about college activities—all from one simple app."

val aboutText5 =
    "This app is specially built for HBTU MCA alumni to maintain lifelong connections and support each other’s professional journey."
@Composable
fun AboutSection(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2033)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(6.dp))
            Text(content, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun AboutFooter() {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2033)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            InfoRow("📦 App Version", APP_VERSION)
            InfoRow("👨‍💻 Developer", DEVELOPER_NAME)
            InfoRow("✉️ Email", DEVELOPER_EMAIL)

            Spacer(Modifier.height(16.dp))

            ClickableRow("🔐 Privacy Policy") {
                openLink(context, PRIVACY_POLICY_URL)
            }

            ClickableRow("📜 Terms & Conditions") {
                openLink(context, TERMS_URL)
            }
        }
    }
}
@Composable
fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White)
        Text(value, color = Color.Gray)
    }
}

@Composable
fun ClickableRow(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        color = Color(0xFF6A5AE0),
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onClick() }
    )
}
fun openLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
