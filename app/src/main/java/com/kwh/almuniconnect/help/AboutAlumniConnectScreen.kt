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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun AboutAlumniConnectScreen(navController: NavController) {

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "About Us ",
                navController = navController
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
               .background(Color.White)
                .padding(paddingValues)
        ) {

            item { AboutHeader() }
            item { AboutSection("🎓 What is AlumniConnect?", aboutText1)
                SectionDividerBlue()
            }
            item { AboutSection("🤝 Our Mission", aboutText2)
                SectionDividerBlue()

            }
            item { AboutSection("🚀 Why AlumniConnect?", aboutText3)
                SectionDividerBlue()

            }
            item { AboutSection("📱 App Features", aboutText4)
                SectionDividerBlue()

            }
            item { AboutSection("💙 Built for HBTU Alumni", aboutText5)
                SectionDividerBlue()

            }
            item { AboutFooter() }
        }
    }
}
@Composable
fun SectionDividerBlue() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        thickness = 0.8.dp,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    )
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
        Text("AlumniConnect",  style = MaterialTheme.typography.titleMedium)
        Text("Connecting Alumni. Creating Opportunities.",style = MaterialTheme.typography.titleMedium)
    }
}
val aboutText1 =
    "AlumniConnect is a dedicated digital platform created to keep the HBTU family connected beyond campus life. It brings together alumni, students, and faculty in one trusted space, helping everyone stay professionally and emotionally connected to their alma mater."

val aboutText2 =
    "Our mission is to strengthen lifelong bonds among HBTU alumni by encouraging mentorship, meaningful networking, and professional collaboration—so every graduate continues to grow, learn, and give back."

val aboutText3 =
    "Unlike general social media platforms, AlumniConnect is built exclusively for the HBTU community. It offers a secure, focused, and respectful environment where alumni can connect with purpose, trust, and shared identity."

val aboutText4 =
    "Through AlumniConnect, you can participate in alumni events, explore career opportunities, connect with seniors, share experiences, and stay updated with campus life—all through one simple and powerful app."

val aboutText5 =
    "Built with pride for HBTU MCA alumni, this platform celebrates our shared journey, professional achievements, and lifelong commitment to supporting one another as one HBTU family."
@Composable
fun AboutSection(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title,  style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(content, color=Color.DarkGray, style = MaterialTheme.typography.labelMedium)
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
        Text(title,  style = MaterialTheme.typography.titleMedium)
        Text(value,  style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ClickableRow(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
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
