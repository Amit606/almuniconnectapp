// kotlin
package com.kwh.almuniconnect.profile

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.api.SignupRequest
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.jobposting.AppTextField
import com.kwh.almuniconnect.login.AuthRepository
import com.kwh.almuniconnect.storage.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    TrackScreen("profile_screen")

    val apiService = remember { NetworkClient.createService(ApiService::class.java) }
    val repository = remember { AuthRepository(apiService) }

    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(repository)
    )

    val apiState by viewModel.state.collectAsState()
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(initial = UserLocalModel())

    Log.e("User", user.name)

    /* ---------- FORM STATE ---------- */
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var fcmToken by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fcmToken = FcmPrefs.getToken(context) ?: ""
    }

    LaunchedEffect(user) {
        if (user.name.isNotBlank()) {
            name = user.name
            email = user.email
            mobile = user.mobile
            branch = user.branch
            year = user.year
            job = user.job
            location = user.location
            birthday = user.birthday
            linkedin = user.linkedin
        }
    }

    val branchOptions = listOf(
        DropdownOption(1, "MCA"),
        DropdownOption(2, "BTech Computer Science"),
        DropdownOption(3, "BTech IT"),
        DropdownOption(4, "BTech ECE"),
        DropdownOption(5, "BTech Electrical"),
        DropdownOption(6, "BTech Mechanical"),
        DropdownOption(7, "BTech Civil")
    )

    var selectedBranch by remember { mutableStateOf<DropdownOption?>(null) }
    val years = (1972..2026).map { it.toString() }
    val safeYear = if (year in years) year else ""

    /* ---------- UI ---------- */

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Profile",
                navController = navController
            )
        },
        containerColor = Color(0xFFF6F7FB)
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {

            LazyColumn {

                /* ---------- PROFILE HEADER ---------- */
                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = user.photo.ifEmpty { R.drawable.man },
                                contentDescription = null,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .border(
                                        3.dp,
                                        MaterialTheme.colorScheme.primary,
                                        CircleShape
                                    )
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = name.ifBlank { "Your Name" },
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = email,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                /* ---------- FORM CARD ---------- */
                item {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            SectionTitle("Personal Details")
                            AppTextField(
                                label = "Name",
                                value = name,
                                imeAction = ImeAction.Next,
                                onValueChange = { name = it }
                            )
                            AppTextField(
                                label = "Email",
                                value = email,
                                onValueChange = { email = it }
                            )

                            AppTextField(
                                label = "Mobile",
                                value = mobile,
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next,
                                onValueChange = {
                                    if (it.all(Char::isDigit) && it.length <= 10)
                                        mobile = it
                                }
                            )

                            SectionTitle("Academic Details")
                            DropdownField(
                                label = "Branch",
                                selected = selectedBranch,
                                items = branchOptions,
                                onSelect = {
                                    selectedBranch = it
                                    branch = it.toString()
                                }
                            )

                            DropdownFieldYear(
                                label = "Passout Year",
                                selected = safeYear,
                                items = years,
                                onSelect = { year = it }
                            )

                            SectionTitle("Professional Details")
                            AppTextField(
                                label = "Job / Company",
                                value = job,
                                onValueChange = { job = it }
                            )
                            AppTextField(
                                label = "Location",
                                value = location,
                                onValueChange = { location = it }
                            )
                            BirthdayPicker(birthday) { birthday = it }
                            AppTextField(
                                label = "LinkedIn URL",
                                value = linkedin,
                                onValueChange = { linkedin = it }
                            )

                            error?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }

                            Spacer(Modifier.height(12.dp))

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(14.dp),
                                enabled = apiState !is ProfileState.Loading,
                                onClick = {
                                    error = validateProfile(
                                        name, email, mobile, branch, year,
                                        job, location, birthday, linkedin
                                    )

                                    if (error == null) {

                                        viewModel.submitProfile(
                                            SignupRequest(
                                                name = name,
                                                mobileNo = mobile,
                                                email = email,
                                                dateOfBirth = uiDateToApi(birthday),
                                                passoutYear = year.toInt(),
                                                courseId = selectedBranch?.id,
                                                countryId = 81,
                                                companyName = job,
                                                title = job,
                                                totalExperience = 0,
                                                linkedinUrl = linkedin,
                                                loggedFrom = "android",
                                                deviceId = DeviceUtils.getDeviceId(context),
                                                fcmToken = fcmToken,
                                                appVersion = "1.0.5",
                                                advertisementId = "",
                                                userAgent = "android"
                                            )
                                        )
                                        navController.navigate(Routes.HOME)
                                    }
                                }
                            ) {
                                if (apiState is ProfileState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(22.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Update Profile")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/* ---------- SMALL UI HELPERS ---------- */

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary
    )
}

/* ---------- BIRTHDAY PICKER ---------- */

@Composable
fun BirthdayPicker(birthday: String, onDate: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dialog = remember {
        DatePickerDialog(
            context,
            { _, y, m, d ->
                onDate("%02d/%02d/%04d".format(d, m + 1, y))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    OutlinedTextField(
        value = birthday,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        label = { Text("Birthday") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dialog.show() }
    )
}

/* ---------- HELPERS ---------- */

fun uiDateToApi(date: String): String {
    val (d, m, y) = date.split("/")
    return "$y-$m-$d"
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