package com.kwh.almuniconnect.help

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.play.integrity.internal.u
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddSocialChannelScreen(navController: NavController) {

    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(SocialType.WHATSAPP) }
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(initial = UserLocalModel())
    val socialTypes = SocialType.values()
    var showConfirmDialog by remember { mutableStateOf(false) }
    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Add Community Channel") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
        ,
        containerColor = Color.White

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            item {

                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {

                        Text(
                            "Submit a Social Channel",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Share alumni communities so others can join.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )

                        /* PLATFORM */

                        Text(
                            "Platform",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            socialTypes.forEach { type ->

                                FilterChip(
                                    selected = selectedType == type,
                                    onClick = { selectedType = type },

                                    label = {
                                        Text(type.name.lowercase().replaceFirstChar { it.uppercase() })
                                    },

                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = getSocialIcon(type)),
                                            contentDescription = type.name,
                                            modifier = Modifier.size(18.dp),
                                            tint = Color.Unspecified
                                        )
                                    }
                                )
                            }
                        }

                        /* CHANNEL TITLE */

                        Column {

                            Text(
                                text = "Channel Title",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )

                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                placeholder = { Text("Enter channel name") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),

                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF1976D2),
                                    unfocusedBorderColor = Color(0xFFB0BEC5)
                                )
                            )
                        }

                        /* DESCRIPTION */

                        Column {

                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                placeholder = { Text("Short description about the community") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),

                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF1976D2),
                                    unfocusedBorderColor = Color(0xFFB0BEC5)
                                )
                            )
                        }

                        /* LINK */

                        Column {

                            Text(
                                text = "Invite Link",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )

                            OutlinedTextField(
                                value = link,
                                onValueChange = { link = it },
                                placeholder = { Text("Paste WhatsApp / Facebook / YouTube link") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),

                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = Color(0xFF1976D2),
                                    unfocusedBorderColor = Color(0xFFB0BEC5)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                if (title.isBlank() || description.isBlank() || link.isBlank()) {
                                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                    showConfirmDialog = true



                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Submit Channel",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        if (showConfirmDialog) {

                            AlertDialog(

                                onDismissRequest = {
                                    showConfirmDialog = false
                                },

                                title = {
                                    Text("Confirm Submission")
                                },

                                text = {
                                    Text("Do you want to submit this community channel for admin approval?")
                                },

                                confirmButton = {

                                    TextButton(
                                        onClick = {

                                            showConfirmDialog = false

                                            submitChannelToFirestore(
                                                type = selectedType,
                                                title = title,
                                                description = description,
                                                link = link,
                                                userId = user.userId,
                                                userEmail = user.email,
                                                userName = user.name,

                                                onSuccess = {

                                                    Toast.makeText(
                                                        context,
                                                        "Channel request sent for approval",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    navController.popBackStack()
                                                },

                                                onError = {

                                                    Toast.makeText(
                                                        context,
                                                        it,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            )
                                        }
                                    ) {
                                        Text("Confirm")
                                    }
                                },

                                dismissButton = {

                                    TextButton(
                                        onClick = {
                                            showConfirmDialog = false
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}
