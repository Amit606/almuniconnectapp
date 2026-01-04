package com.kwh.almuniconnect.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kwh.almuniconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniLoginScreen(
    onRequestOtp: (String) -> Unit = {},
    onLoginWithPassword: () -> Unit = {},
    onGoogleLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())   // ✅ SCROLL ENABLED
                .imePadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(60.dp))

            // 🔵 Logo (HBTU Royal Blue)
            Box(
                modifier = Modifier
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hbtu_logo),
                    contentDescription = "HBTU Logo",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(Modifier.height(24.dp))

            // 🏷 Title
            Text(
                text = "HBTU\nALUMNI\nNETWORK",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "POWERED BY AppsChance",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(Modifier.height(36.dp))

            Text(
                text = "Let’s get started",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(20.dp))

            // ✉️ Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter your email*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(Modifier.height(20.dp))

            // 🔵 Request OTP (Royal Blue)
            Button(
                onClick = { onRequestOtp(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Request OTP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(14.dp))

            // 🔗 Login with Password (Sage Green accent)
            Text(
                text = "Login with Password",
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onLoginWithPassword() }
            )

            Spacer(Modifier.height(24.dp))

            // ─── OR ───
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.width(80.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
                Text(
                    text = "  OR  ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
                Divider(
                    modifier = Modifier.width(80.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
            }

            Spacer(Modifier.height(20.dp))

            // 🌐 Google Login (Outlined – Royal Blue)
            OutlinedButton(
                onClick = { onGoogleLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Continue with Google",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

