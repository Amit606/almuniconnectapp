package com.kwh.almuniconnect.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniLoginScreen(
    onBack: () -> Unit = {},
    onRequestOtp: (String) -> Unit = {},
    onLoginWithPassword: () -> Unit = {},
    onGoogleLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    TrackScreen("alumni_login_screen")

    Scaffold(
//
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(32.dp))

            // 🔵 Logo
            Image(
                painter = painterResource(R.drawable.playstore),
                contentDescription = "HBTU Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(Modifier.height(24.dp))

            // 🏷 Heading
            Text(
                text = "Sign in",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "to continue to Harcourtian Alumni Network",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // ─── OR ───
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(Modifier.weight(1f))
                Text(
                    "  or  ",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Divider(Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // 🌐 Google Sign-In
            OutlinedButton(
                onClick = onGoogleLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.man),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "Continue with Google",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(32.dp))

            // 🔐 Footer
            Text(
                text = "By continuing, you agree to our Terms & Privacy Policy",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
