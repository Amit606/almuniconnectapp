package com.kwh.almuniconnect.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kwh.almuniconnect.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordLoginScreen(
    navController: NavHostController,
    email: String = "",
    onBack: () -> Unit = {},
    onLogin: (String, String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {}
) {
    var userEmail by remember { mutableStateOf(email) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val isValid = userEmail.isNotBlank() && password.length >= 6

    Scaffold(
        containerColor = Color(0xFF0E1420),
        topBar = {
            TopAppBar(
                title = { Text("Login", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0E1420)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .background(Color(0xFF0E1420)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Login with Password",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Welcome back! Please enter your details",
                fontSize = 14.sp,
                color = Color(0xFFB0B8C8)
            )

            Spacer(Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value = userEmail,
                onValueChange = { userEmail = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Email address", color = Color(0xFF9AA4B2)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = darkTextFieldColors()
            )

            Spacer(Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Password", color = Color(0xFF9AA4B2)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            null,
                            tint = Color(0xFF9AA4B2)
                        )
                    }
                },
                colors = darkTextFieldColors()
            )

            Spacer(Modifier.height(12.dp))

            // Forgot
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    "Forgot Password?",
                    color = Color(0xFF6EA8FF),
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onForgotPassword() }
                )
            }

            Spacer(Modifier.height(32.dp))

            // Login button (color NEVER fades)
            Button(
                onClick = {
                  //  onLogin(userEmail, password)
                    navController.navigate(Routes.HOME)
                },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2F6BFF),
                    disabledContainerColor = Color(0xFF2F6BFF), // 👈 keep color
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                )
            ) {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/* Dark theme TextField colors */
@Composable
private fun darkTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF2F6BFF),
    unfocusedBorderColor = Color(0xFF3A455A),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color(0xFF2F6BFF)
)
