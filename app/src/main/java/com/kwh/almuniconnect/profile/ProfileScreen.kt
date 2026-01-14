package com.kwh.almuniconnect.profile

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.jobposting.AppTextField
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val userPrefs = remember { UserPreferences(context) }

    val user by userPrefs.getUser().collectAsState(
        initial = UserLocalModel()
    )

    var name by remember(user.name) { mutableStateOf(user.name) }
    var email by remember(user.email) { mutableStateOf(user.email) }
    var mobile by remember(user.mobile) { mutableStateOf(user.mobile) }
    var branch by remember(user.branch) { mutableStateOf(user.branch) }
    var year by remember(user.year) { mutableStateOf(user.year) }
    var job by remember(user.job) { mutableStateOf(user.job) }
    var location by remember(user.location) { mutableStateOf(user.location) }
    var birthday by remember(user.birthday) { mutableStateOf(user.birthday) }
    var linkedin by remember(user.linkedin) { mutableStateOf(user.linkedin) }
    var error by remember { mutableStateOf<String?>(null) }

    val profileGradient = Brush.verticalGradient(
        listOf(
            Color(0xFF0D1B2A),
            Color(0xFF1B4DB1),
            Color(0xFF3A7BD5)
        )
    )

    val branches = listOf(
        "B.Tech – CSE","B.Tech – IT","B.Tech – ECE","B.Tech – EE",
        "B.Tech – ME","B.Tech – CE","M.Tech","MCA","MBA","BCA"
    )
    val years = (1972..2026).map { it.toString() }
    val safeBranch = if (branch in branches) branch else ""
    val safeYear = if (year in years) year else ""
    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Profile  ",
                navController = navController
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(profileGradient)
                .padding(paddingValues)
        ) {
            LazyColumn {

                item {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = user.photo.ifEmpty { R.drawable.man },
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color.White, CircleShape)
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Column(Modifier.padding(12.dp)) {

                            AppTextField("Name", name) { name = it }
                            AppTextField("Email", email) { email = it }

                            AppTextField(
                                label = "Mobile",
                                value = mobile,
                                onValueChange = {
                                    if (it.all(Char::isDigit) && it.length <= 10)
                                        mobile = it
                                }
                            )

                            Spacer(Modifier.height(8.dp))
                            DropdownField("Branch", safeBranch, branches) { branch = it }
                            Spacer(Modifier.height(8.dp))
                            DropdownField("Year", safeYear, years) { year = it }
                            AppTextField("Job / Company", job) { job = it }
                            AppTextField("Location", location) { location = it }

                            BirthdayPicker(birthday) { birthday = it }

                            AppTextField(
                                label = "LinkedIn URL",
                                value = linkedin,
                                onValueChange = { linkedin = it }
                            )

                            error?.let {
                                Text(it, color = Color.Red)
                            }

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    error = validateProfile(
                                        name, email, mobile, branch, year,
                                        job, location, birthday, linkedin
                                    )

                                    if (error == null) {
                                        scope.launch {
                                            userPrefs.saveProfile(
                                                user.copy(
                                                    name = name,
                                                    email = email,
                                                    mobile = mobile,
                                                    branch = branch,
                                                    year = year,
                                                    job = job,
                                                    location = location,
                                                    birthday = birthday,
                                                    linkedin = linkedin
                                                )
                                            )
                                            navController.navigate("home") {
                                                popUpTo("profile") { inclusive = true }
                                            }
                                        }
                                    }
                                }
                            ) {
                                Text("Update Profile")
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------------- HELPERS ---------------- */

fun validateProfile(
    name: String,
    email: String,
    mobile: String,
    branch: String,
    year: String,
    job: String,
    location: String,
    birthday: String,
    linkedin: String
): String? {
    if (name.isBlank()) return "Enter name"
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Invalid email"
    if (mobile.length != 10) return "Enter valid mobile"
    if (branch.isBlank()) return "Select branch"
    if (year.isBlank()) return "Select year"
    if (job.isBlank()) return "Enter job"
    if (location.isBlank()) return "Enter location"
    if (birthday.isBlank()) return "Select birthday"
    if (linkedin.isNotBlank() && !linkedin.startsWith("http")) return "Invalid LinkedIn URL"
    return null
}

@Composable
fun BirthdayPicker(birthday: String, onDate: (String) -> Unit) {
    val context = LocalContext.current
    val cal = Calendar.getInstance()

    val dialog = remember {
        DatePickerDialog(
            context,
            { _, y, m, d ->
                onDate("%02d/%02d/%04d".format(d, m + 1, y))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    OutlinedTextField(
        value = birthday,
        onValueChange = {},
        readOnly = true,
        label = { Text("Birthday") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dialog.show() }
    )
}
