package com.kwh.almuniconnect.login

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.kwh.almuniconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniLoginScreen(
    onGoogleLogin: () -> Unit = {},
    onOpenTerms: () -> Unit = {},
    onOpenPrivacy: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    var totalAlumni by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        visible = true
    }
    LaunchedEffect(Unit) {
        FirebaseFirestore.getInstance()
            .collection("stats")
            .document("app_stats")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e("FIRESTORE", "Error: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val value = snapshot.getLong("total_users")
                    Log.d("FIRESTORE", "Value: $value")

                    totalAlumni = value ?: 0
                } else {
                    Log.d("FIRESTORE", "Document not found")
                }
            }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(600)) +
                        scaleIn(initialScale = 0.92f, animationSpec = tween(600))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // App Icon
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(92.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.playstore),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(88.dp)
                                    .shadow(8.dp, RoundedCornerShape(20.dp))
                            )
                        }

                        Spacer(Modifier.height(32.dp))

                        Text(
                            text = "Welcome Back",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Sign in to continue to\nAlumni Network",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )


                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "$totalAlumni+ alumni are already here — join them today",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(48.dp))

                        // Google Button
                        FilledTonalButton(
                            onClick = onGoogleLogin,
                            enabled = isChecked,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_google),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Continue with Google",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(Modifier.height(40.dp))

                        // Checkbox + Agreement Text
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary
                                )
                            )

                            Spacer(Modifier.width(8.dp))

                            Text(
                                text = "I agree to the Terms of Service and Privacy Policy",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        }

                        // Clickable Links (Clean & Professional)
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Terms of Service",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable { onOpenTerms() }
                            )

                            Spacer(Modifier.width(20.dp))

                            Text(
                                text = "Privacy Policy",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable { onOpenPrivacy() }
                            )
                        }
                    }
                }
            }
        }
    }
}