// LoginScreen.kt
// Jetpack Compose login screen with Google Sign-In and normal email/password login UI

package com.kwh.almuniconnect.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Replace with your project's resources
// import com.example.R

@Composable
fun LoginScreen(
    onLogin: (email: String, password: String) -> Unit,
    onGoogleSignIn: () -> Unit,
    onForgotPassword: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    fun validateAndLogin() {
        showError = email.isBlank() || password.length < 6
        if (!showError) {
            isLoading = true
            try {
                onLogin(email.trim(), password)
            } finally {
                isLoading = false
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Color(0xFF0E1420)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome back", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Sign in to continue", color = Color.LightGray,style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email",color = Color.White) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password",color = Color.LightGray) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, tint = Color.White, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    validateAndLogin()
                }),
                modifier = Modifier
                    .fillMaxWidth()
            )

            if (showError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please enter a valid email and password (min 6 chars)",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    validateAndLogin()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF142338)
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Sign in",color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onForgotPassword) {
                Text("Forgot password?",color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // OR divider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Divider(modifier = Modifier.weight(1f))
                Text("  OR  ",color = Color.LightGray)
                Divider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Google Sign In button (UI only)
            OutlinedButton(
                onClick = onGoogleSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF142338)
                ),                shape = RoundedCornerShape(12.dp)
            ) {
                // Provide your Google icon in drawable and replace resource id below
                // Image(painter = painterResource(id = R.drawable.ic_google_logo), contentDescription = "Google")
                // spacer + text
                Text("Continue with Google",color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("Don't have an account? ",color = Color.White)
                Text(
                    text = "Create one",
                    color = Color.White,
                    modifier = Modifier.clickable(onClick = onCreateAccount),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(onLogin = { _, _ -> }, onGoogleSignIn = { })
    }
}

// USAGE NOTES:
// - This file provides UI only. For Google Sign-In you need to wire up GoogleSignInClient
//   in your Activity and call the appropriate launch (startActivityForResult or ActivityResultContract) to obtain the ID token.
// - After receiving the Google ID token, exchange it with FirebaseAuth or your backend to authenticate the user.
// - The `onLogin` callback should perform the actual authentication (e.g. call repository, ViewModel). Handle UI state accordingly.
// - Replace any resource references (icons) with your project's assets.
