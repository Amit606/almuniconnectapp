package com.kwh.almuniconnect.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
    navController: NavController,
    onUpgradeClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color(0xFF0E1420),
        topBar = {
            TopAppBar(
                title = { Text("Premium", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420)
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)              // prevents overlap
               // .systemBarsPadding()                // handles notch & gesture bars
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = "Alumni Connect Premium",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Lifetime Access – Pay once, use forever",
                fontSize = 14.sp,
                color = Color(0xFFB0B0B0)
            )

            Spacer(Modifier.height(10.dp))

            // Price Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF142338))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()                               // centers horizontally
                        .wrapContentHeight(Alignment.CenterVertically) // centers vertically
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Only",
                        color = Color.White
                    )

                    Text(
                        text = "₹199",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "One-time payment",
                        color = Color(0xFFB0B0B0)
                    )
                }
            }


            Spacer(Modifier.height(24.dp))

            Text(
                "What you get",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(12.dp))

            PremiumFeature("Direct chat with alumni")
            PremiumFeature("View phone & email")
            PremiumFeature("Job referrals & opportunities")
            PremiumFeature("Profile visibility boost")
            PremiumFeature("Alumni events access")
            PremiumFeature("Ad-free experience")

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = onUpgradeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),     // prevents small-screen cut
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Text("Upgrade to Premium – ₹199", fontSize = 16.sp)
            }

            Spacer(Modifier.height(2.dp))

            Text(
                "Secure payment powered by Google Play",
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(5.dp)) // safe bottom space
        }
    }
}


@Composable
fun PremiumFeature(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

