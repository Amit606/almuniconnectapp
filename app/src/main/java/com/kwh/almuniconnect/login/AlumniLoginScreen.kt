package com.kwh.almuniconnect.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.ui.graphics.Color
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
    TrackScreen("alumni_login_screen")

    // 🎬 Animation state
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Scaffold { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + scaleIn(initialScale = 0.95f)
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // 🔵 Logo
                        Image(
                            painter = painterResource(R.drawable.playstore),
                            contentDescription = "HBTU Logo",
                            modifier = Modifier.size(88.dp)
                        )

                        Spacer(Modifier.height(24.dp))

                        // 🏷 Heading
                        Text(
                            text = "Sign in",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "to continue to Harcourtian Alumni Network",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(32.dp))

                        // ─── OR ───
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
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

                        // 🌐 Material Filled Tonal Google Button
                        FilledTonalButton(
                            onClick = onGoogleLogin,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),

                            shape = RoundedCornerShape(26.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_google),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Continue with Google",
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
        }
    }
}

