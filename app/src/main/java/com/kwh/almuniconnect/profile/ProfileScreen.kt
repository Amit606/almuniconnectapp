package com.kwh.almuniconnect.profile

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.api.SignupRequest
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.jobposting.AppTextField
import com.kwh.almuniconnect.login.AuthRepository
import com.kwh.almuniconnect.storage.FcmPrefs
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
import com.kwh.almuniconnect.storage.UserSession
import java.util.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    // 1️⃣ Create ApiService
    val apiService = remember {
        NetworkClient.createService(ApiService::class.java)
    }
    TrackScreen("profile_screen")

    // 2️⃣ Repository
    val repository = remember {
        AuthRepository(apiService)
    }

    // 3️⃣ ViewModel WITH FACTORY (CRITICAL)
    val viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(repository)
    )

    val apiState by viewModel.state.collectAsState()


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
    var fcmToken by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fcmToken = FcmPrefs.getToken(context) ?: ""
    }


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
                .background(Color.White)
                .padding(paddingValues)
                .imePadding() // ✅ keyboard resize
                .clickable( // ✅ dismiss keyboard on outside tap
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                }
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

                            AppTextField("Name", name, onValueChange =  { name = it })
                            AppTextField("Email", email,onValueChange =  { email = it })

                            AppTextField(
                                label = "Mobile",
                                value = mobile,
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next,
                                onDone = { focusManager.clearFocus() },
                                onValueChange = {
                                    if (it.all(Char::isDigit) && it.length <= 10) {
                                        mobile = it
                                    }
                                }
                            )

                            Spacer(Modifier.height(8.dp))
                            DropdownField("Branch", safeBranch, branches) { branch = it }
                            Spacer(Modifier.height(8.dp))
                            DropdownField("Year", safeYear, years) { year = it }
                            AppTextField("Job / Company", job,onValueChange =   { job = it })
                            AppTextField("Location", location,onValueChange =  {  location = it })

                            BirthdayPicker(birthday) { birthday = it }

                            AppTextField(
                                label = "LinkedIn URL",
                                value = linkedin,
                                imeAction = ImeAction.Done,
                                onValueChange = { linkedin = it }
                            )

                            error?.let {
                                Text(it, color = Color.Red)
                            }

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = apiState !is ProfileState.Loading,
                                onClick = {
                                    error = validateProfile(
                                        name, email, mobile, branch, year,
                                        job, location, birthday, linkedin
                                    )

                                    if (error == null) {
                                        val deviceId = DeviceUtils.getDeviceId(context)

                                        viewModel.submitProfile(

                                            SignupRequest(
                                                name = name,
                                                mobileNo = mobile,
                                                email = email,
                                                dateOfBirth = uiDateToApi(birthday),
                                                passoutYear = year.toInt(),
                                                courseId = 1,
                                                countryId = 1,
                                                companyName = job,
                                                title = job,
                                                totalExperience = 0,
                                                linkedinUrl = linkedin,
                                                loggedFrom = "android",
                                                deviceId = deviceId,
                                                fcmToken = fcmToken,
                                                appVersion = "1.0.1",
                                                advertisementId = "",
                                                userAgent = "android"
                                            )
                                        )
                                    }
                                }
                            ) {
                                if (apiState is ProfileState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Update Profile")
                                }
                            }
                        }
                    }

                    LaunchedEffect(apiState) {
                        when (apiState) {
                            is ProfileState.Success -> {
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
                                UserSession.saveLogin(context)

                                navController.navigate("home") {
                                    popUpTo("profile") { inclusive = true }
                                }
                            }

                            is ProfileState.Error -> {
                                error = (apiState as ProfileState.Error).message
                            }

                            else -> Unit
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
fun BirthdayPicker(
    birthday: String,
    onDate: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                onDate("%02d/%02d/%04d".format(day, month + 1, year))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() }
    ) {
        OutlinedTextField(
            value = birthday,
            onValueChange = {},
            readOnly = true,
            label = { Text("Birthday", color = Color.Black) },
            enabled = false, // prevents keyboard
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}
fun uiDateToApi(date: String): String {
    val (d, m, y) = date.split("/")
    return "$y-$m-$d"
}