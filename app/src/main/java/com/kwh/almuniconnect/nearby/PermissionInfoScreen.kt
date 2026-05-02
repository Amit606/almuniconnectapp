package com.kwh.almuniconnect.nearby

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionInfoScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Permission") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            SectionCard(
                title = "📍 Why we need your location"
            ) {
                Text(
                    "We use your location to help you discover verified Harcourtians nearby, making it easier to connect, network, and attend local meetups.",
                    color = Color.Gray
                )
            }

            SectionCard(
                title = "🔒 How your data is used"
            ) {
                Bullet("Used only to show nearby Harcourtians")
                Bullet("Only approximate location is stored (not exact address)")
                Bullet("Never shared with other users")
                Bullet("Not used for tracking your movement")
            }

            SectionCard(
                title = "👤 Your control"
            ) {
                Bullet("You can turn off location anytime from settings")
                Bullet("You control when and how this feature is used")
            }

            SectionCard(
                title = "✅ Trusted community"
            ) {
                Text(
                    "This feature is built exclusively for verified Harcourtian alumni to help build meaningful professional connections.",
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "🔐 Your location is securely used only for nearby discovery and is never publicly visible.",
                color = Color(0xFF2E7D32),
                fontSize = 13.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
@Composable
fun Bullet(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text("• ", fontWeight = FontWeight.Bold)
        Text(text, color = Color.Gray, fontSize = 14.sp)
    }
}
@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            content()
        }
    }
}