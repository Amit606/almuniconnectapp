package com.kwh.almuniconnect.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Help & Support ")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White

                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420),
                    titleContentColor = Color.White
                )
            )
        },
        contentColor = Color(0xFF0E1420)
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0E1420))
                .padding(paddingValues)
        ) {

            item { SupportHeader() }

            item {
                SupportCard(
                    "📌 About AlumniConnect",
                    "Learn how to use AlumniConnect and connect with seniors.",
                    navController
                )
            }
            item { ContactSupportSection() }
        }
    }
}
    @Composable
    fun SupportHeader() {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "We’re here to help you connect better with your alumni network.",
                color = Color.Gray
            )
        }
    }

@Composable
fun SupportCard(title: String, subtitle: String,navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2033)),
        onClick = {navController.navigate(Routes.ABOUT_US)},
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = Color.White)
            Text(subtitle, color = Color.Gray, fontSize = 13.sp)
        }
    }
}
@Composable
fun ContactSupportSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Contact Support", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

        Spacer(Modifier.height(12.dp))

        SupportAction("📧 Email Support", "support@alumniconnect.com")
        SupportAction("💬 WhatsApp Support", "+91 9876543210")
        SupportAction("🌐 Visit Website", "www.alumniconnect.com")
    }
}

@Composable
fun SupportAction(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White)
        Text(value, color = Color(0xFF6A5AE0))
    }
}



