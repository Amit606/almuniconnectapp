package com.kwh.almuniconnect.profile

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.api.*
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.jobposting.AppTextField
import com.kwh.almuniconnect.jobposting.SectionTitle
import com.kwh.almuniconnect.login.AuthRepository
import com.kwh.almuniconnect.storage.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val apiService = remember { NetworkClient.createService(ApiService::class.java) }
    val repository = remember { AuthRepository(apiService) }

    val viewModel: ProfileViewModel =
        viewModel(factory = ProfileViewModelFactory(repository))

    val apiState by viewModel.state.collectAsState()

    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(initial = UserLocalModel())



    var selectedBranch by remember { mutableStateOf<Branch?>(null) }
    var branch by remember { mutableStateOf("") }
    /* ---------- FORM STATE ---------- */

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var totalExp by remember { mutableStateOf<Int?>(null) }
    val totalYear = (1..30).toList()
    var job by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var fcmToken by remember { mutableStateOf("") }
    val years = (1972..2026).map { it.toString() }
    val branchNames = branches.map { it.name }
    var selectedBranchId by remember { mutableStateOf<Int?>(null) }



    val safeYear = if (year in years) year else ""
    LaunchedEffect(Unit) {
        fcmToken = FcmPrefs.getToken(context) ?: ""
    }

    LaunchedEffect(user) {
        name = user.name
        email = user.email
        mobile = user.mobile
        branch = user.branch
        year = user.year
        job = user.job
        location = user.location
        birthday = user.birthday
        linkedin = user.linkedin
        totalExp = user.totalExp
    }

    /* ---------- HANDLE API SUCCESS ---------- */

    LaunchedEffect(apiState) {

        when (apiState) {

            is ProfileState.Success -> {

                val profile =
                    (apiState as ProfileState.Success).profile

                userPrefs.saveProfile(
                    UserLocalModel(
                        userId = profile.userId ?: "",
                        name = profile.name ?: "",
                        email = profile.email ?: "",
                        mobile = profile.mobileNo ?: "",
                        branch = profile.courseName ?: "",
                        branchId = profile.courseId ?: 0,
                        year = profile.passoutYear?.toString() ?: "",
                        job = profile.companyName ?: "",
                        location = "",
                        birthday = profile.dateOfBirth ?: "",
                        linkedin = profile.linkedinUrl ?: "",
                        photo = profile.photoUrl ?: "",
                        totalExp = profile.totalExperience ?: 0

//                        accessToken = profile.accessToken ?: "",
//                        accessTokenExpiry = profile.accessTokenExpiry ?: "",
//                        refreshToken = profile.refreshToken ?: "",
//                        refreshTokenExpiry = profile.refreshTokenExpiry ?: ""
                    )
                )

                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.PROFILE) { inclusive = true }
                }
            }

            is ProfileState.Error -> {
                error = (apiState as ProfileState.Error).message
            }

            else -> {}
        }
    }

    /* ---------- UI ---------- */

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Profile",
                navController = navController
            )
        },
        containerColor = Color.White
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                /* ---------- PROFILE HEADER ---------- */

                item {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {

                        Box {

                            // ✅ Background Image
                            Image(
                                painter = painterResource(id = R.drawable.hbtu), // your image
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .matchParentSize()
                            )

                            // ✅ Optional overlay (for readability)
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.15f))
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                AsyncImage(
                                    model = user.photo.ifEmpty { R.drawable.man },
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
                                )

                                Spacer(Modifier.height(14.dp))

                                Text(
                                    text = name.ifBlank { "Your Name" },
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,   // ✅ Bold
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(2f, 2f),
                                            blurRadius = 6f
                                        )
                                    )
                                )

                                Text(
                                    text = email.ifBlank { "Your Email ID" },
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                /* ---------- FORM CARD ---------- */

                item {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            SectionTitle("Personal Details")

                            AppTextField("Name", name) { name = it }
                            AppTextField(
                                label = "Email",
                                value = email,
                                readOnly = true,
                            ) { email = it }

                            AppTextField(
                                label = "Mobile",
                                value = mobile,
                                keyboardType = KeyboardType.Number
                            ) {
                                if (it.all(Char::isDigit) && it.length <= 10)
                                    mobile = it
                            }

                            SectionTitle("Professional Details")
                            DropdownFieldYear(
                                label = "Branch",
                                selected = branch,
                                items = branchNames,
                                onSelect = { selectedName ->

                                    branch = selectedName

                                    // find full object from name
                                    val branchObj = branches.find { it.name == selectedName }
                                    selectedBranchId = branchObj?.id
                                }
                            )

                            DropdownFieldYear( label = "PassOut  Year", selected = safeYear,
                                items = years, onSelect = { year = it } )

                            AppTextField("Job / Company", job) { job = it }
                            // ✅ FIXED TOTAL EXP
                            DropdownFieldYear(
                                label = "Total Exp",
                                selected = totalExp,
                                items = totalYear,
                                onSelect = { totalExp = it }
                            )

                            AppTextField("Location", location) { location = it }

                            BirthdayPicker(birthday) { birthday = it }

                            AppTextField("LinkedIn URL", linkedin) {
                                linkedin = it
                            }

                            error?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                enabled = apiState !is ProfileState.Loading,
                                onClick = {

                                    error = validateProfile(
                                        name, email, mobile,
                                        branch, year,
                                        job, location,
                                        birthday, linkedin
                                    )

                                    if (error == null) {
                                        viewModel.submitProfile(
                                            SignupRequest(
                                                name = name,
                                                mobileNo = mobile,
                                                email = email,
                                                dateOfBirth = birthday,
                                                passoutYear = year.toIntOrNull() ?: 0,
                                                courseId = selectedBranchId,
                                                countryId = 81,
                                                companyName = job,
                                                title = job,
                                                totalExperience = totalExp ?: 0,
                                                linkedinUrl = linkedin,
                                                loggedFrom = "android",
                                                deviceId = DeviceUtils.getDeviceId(context),
                                                fcmToken = fcmToken,
                                                appVersion = "1.0.5",
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
                                    Text(
                                        "Update Profile",
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayPicker(
    birthday: String,
    onDateSelected: (String) -> Unit
) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val formatted =
                    "%02d-%02d-%02d".format(year, month + 1, day)
                onDateSelected(formatted)
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
        enabled = true,   // ✅ IMPORTANT
        label = { Text("Birthday") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                datePickerDialog.show()
            },
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            IconButton(onClick = {
                datePickerDialog.show()
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date"
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        textStyle = TextStyle(fontSize = 15.sp)
    )
}

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

    if (name.isBlank())
        return "Please enter your name"

    if (name.length < 3)
        return "Name must be at least 3 characters"

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return "Please enter a valid email"

    if (mobile.length != 10)
        return "Mobile number must be 10 digits"

    if (branch.isBlank())
        return "Please select branch"

    if (year.isBlank())
        return "Please select passout year"

    if (job.isBlank())
        return "Please enter job/company"

    if (location.isBlank())
        return "Please enter location"

    if (birthday.isBlank())
        return "Please select birthday"

    if (linkedin.isNotBlank() &&
        !linkedin.startsWith("http://") &&
        !linkedin.startsWith("https://")
    )
        return "LinkedIn URL must start with http:// or https://"

    return null
}
