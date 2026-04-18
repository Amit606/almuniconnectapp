package com.kwh.almuniconnect.subscription

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.billing.BillingViewModel
import com.kwh.almuniconnect.billing.PremiumAnalytics
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
    navController: NavController,
    viewModel: BillingViewModel = viewModel()
) {

    val context = LocalContext.current
    val activity = context as Activity
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(
        initial = UserLocalModel()
    )
    val isPremium = viewModel.isPremium.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val userId = user.userId ?: ""
    val email = user.email ?: ""

    LaunchedEffect(isPremium) {
        if (isPremium) {

            Toast.makeText(context, "Premium Unlocked 🎉", Toast.LENGTH_SHORT).show()

            delay(800)

            navController.navigate(Routes.NEARBY_HARCOURTIANS) {
                popUpTo(Routes.SUBSCRIPTION) { inclusive = true }
            }
        }
    }
    LaunchedEffect(Unit) {
        PremiumAnalytics.logPaywallViewed(context)
    }
    LaunchedEffect(Unit) {
        viewModel.startBilling()
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Unlock Premium",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF7F9FC))
        ) {

            // 🔥 HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF1E3C72),
                                Color(0xFF2A5298)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {

                    Text(
                        text = "Unlock Direct Alumni Connections",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Get full access to contact details instantly",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // 💳 PRICE CARD
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.width(320.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text("ONLY", fontSize = 12.sp, color = Color.Gray)

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "₹199",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1E88E5)
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            "One-time unlock • Lifetime access",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "⚡ Limited launch price",
                            fontSize = 12.sp,
                            color = Color(0xFFFF6F00)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // 🔍 PREVIEW (VERY IMPORTANT)
            PreviewCard()

            Spacer(Modifier.height(20.dp))

            // 🏷 FEATURES
            Text(
                text = "What you get",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            FeatureCard {
                PremiumFeature("Unlock phone number & WhatsApp access")
                PremiumFeature("Directly contact alumni for referrals")
                PremiumFeature("View full contact details instantly")
                PremiumFeature("Connect with alumni near you")
                PremiumFeature("Priority visibility in network")
                PremiumFeature("Ad-free premium experience")
            }

            Spacer(Modifier.height(24.dp))

            // 💙 TRUST CARD
            AlumniSupportMessage()

            Spacer(Modifier.height(24.dp))

            // 🚀 CTA BUTTON
            Button(
                onClick = {
                    if (!isPremium && !isLoading) {
                        PremiumAnalytics.logPurchaseClick(context)

                        viewModel.buy(activity,email,userId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (isPremium) "You're Premium 🎉" else "Unlock Now – ₹199",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "🔒 Secure payment via Google Play • No subscription",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}
@Composable
fun PreviewCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text("Preview", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Mobile: ********40")
            Text("Email: ********@gmail.com")

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "🔓 Unlock to view full details",
                color = Color(0xFF1E88E5)
            )
        }
    }
}

@Composable
fun FeatureCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun PremiumFeature(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            color = Color(0xFF333333)
        )
    }
}

@Composable
fun AlumniSupportMessage() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1F6FF)
        ),
        border = BorderStroke(1.dp, Color(0xFFD6E4FF))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Built by Alumni, for Alumni",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E3C72)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "This platform is sustained by alumni support. " +
                        "A small one-time contribution helps us maintain the app, " +
                        "improve features, and keep the HBTU alumni network strong.",
                fontSize = 14.sp,
                color = Color(0xFF444444),
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "₹199 • Pay once • Support forever",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E88E5)
            )
        }
    }
}
