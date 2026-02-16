package com.kwh.almuniconnect.settings

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.Routes.PRIVACY_POLICY_URL
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.help.openLink
import com.kwh.almuniconnect.storage.UserSession
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    TrackScreen("settings_screen")
    val context = LocalContext.current

    val apiService = remember { NetworkClient.createService(ApiService::class.java) }
    val repository = remember { UserRepository(apiService, context) }
    val factory = remember { SettingsViewModelFactory(repository) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val viewModel: SettingsViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)

    val deleteState by viewModel.deleteState.collectAsState()
    val loading by viewModel.loading.collectAsState()
    // 🔹 Navigate when deleted
    LaunchedEffect(deleteState) {
        deleteState?.let { success ->
            if (success) {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0)
                }
            }
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Settings",
                navController = navController
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            item {
                SectionHeader("About Us")
                SettingItem(
                    icon = Icons.Default.Info,
                    title = "About AlumniConnect",
                    subtitle = "Learn how to connect with seniors and alumni",
                    onClick = { navController.navigate(Routes.ABOUT_US) }

                )
            }


            item {
                SectionHeader("Account")
                SettingItem(
                    icon = Icons.Default.Person,
                    title = "Edit Profile",
                    subtitle = "Update your personal details",
                    onClick = { navController.navigate(Routes.USER_PROFILE) }
                )
            }

            item {
                SectionHeader("Preferences")
                SettingItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Manage notification settings",
                    onClick = { /* navigate */ }
                )
                SettingItem(
                    icon = Icons.Default.Delete,
                    title = "Delete All Data",
                    subtitle = "Permanently remove all saved information from this device",
                    onClick = { showDialog = true  }
                )
            }

            item {
                SectionHeader("Support")
                SettingItem(
                    icon = Icons.Default.Lock,
                    title = "Privacy Policy",
                    subtitle = "Read our privacy policy",
                    onClick = {openLink(context, PRIVACY_POLICY_URL)
                    }
                )

                SettingItem(
                    icon = Icons.Default.Help,
                    title = "Help & Support",
                    subtitle = "Get help or contact support",
                    onClick = { navController.navigate(Routes.HELP_SUPPORTS) }
                )
            }

            item {
                SectionHeader("Account Actions")
                SettingItem(
                    icon = Icons.Default.Logout,
                    title = "Logout",
                    subtitle = "Sign out from your account",
                    titleColor = MaterialTheme.colorScheme.error,
                    iconColor = MaterialTheme.colorScheme.error,
                    onClick = {
                       showLogoutDialog =true

                    }
                )
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Confirm Logout") },
                text = { Text("Are you sure you want to logout from your account?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            scope.launch {
                                UserSession.logout(context)
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Text("Logout", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.deleteAccount()
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Delete Account?") },
                text = { Text("This action cannot be undone.") }
            )
        }
    }
}
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}
fun logoutUser(context: Context, navController: NavController) {

    // 1️⃣ Firebase Logout
    FirebaseAuth.getInstance().signOut()

    // 2️⃣ Google Sign Out
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    googleSignInClient.signOut()

    // 3️⃣ Clear Local Session (Your custom session)


    // 4️⃣ Navigate to Login & Clear Backstack
    navController.navigate("login") {
        popUpTo(0) { inclusive = true }
    }
}
@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    iconColor: Color = Color(0xFF1E88E5), // ✅ Blue icon
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = titleColor
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Divider(
            modifier = Modifier.padding(start = 56.dp),
            thickness = 0.5.dp
        )
    }
}
