package com.kwh.almuniconnect.feedback

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences

@Composable
fun FeedbackForm(navController: NavController) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(initial = UserLocalModel())
    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Feedback",
                showBack = true,
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Your Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Your Feedback") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                enabled = !isLoading
            )

            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || message.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true



                    submitFeedback(
                        context = context,
                        userId = user.userId ?: "anonymous",
                        name = user.name ?: name,
                        email = user.email ?: email,
                        message = message,
                        onSuccess = {
                            isLoading = false
                            Toast.makeText(context, "Feedback Submitted ✅", Toast.LENGTH_SHORT)
                                .show()

                            name = ""
                            email = ""
                            message = ""
                        },
                        onError = {
                            isLoading = false
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        }
                    )

                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Submit")
                }
            }
        }
    }
}

fun submitFeedback(
    context: android.content.Context,
    userId: String,
    name: String,
    email: String,
    message: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {

    val db = FirebaseFirestore.getInstance()

    val packageInfo = context.packageManager
        .getPackageInfo(context.packageName, 0)

    val versionName = packageInfo.versionName
    val versionCode = packageInfo.longVersionCode

    val feedbackData = hashMapOf(
        "userId" to userId,
        "name" to name,
        "email" to email,
        "message" to message,
        "appVersionName" to versionName,
        "appVersionCode" to versionCode,
        "manufacturer" to Build.MANUFACTURER,
        "model" to Build.MODEL,
        "androidVersion" to Build.VERSION.RELEASE,
        "timestamp" to System.currentTimeMillis()
    )

    db.collection("feedback")
        .add(feedbackData)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener {
            onError(it.message ?: "Failed to submit feedback")
        }
}