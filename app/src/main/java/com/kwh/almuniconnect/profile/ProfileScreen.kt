package com.kwh.almuniconnect.profile
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kwh.almuniconnect.jobposting.AppTextField
import com.kwh.almuniconnect.network.AlumniProfile

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.jobposting.AppTextField
import com.kwh.almuniconnect.storage.UserLocalModel
import com.kwh.almuniconnect.storage.UserPreferences
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val userPrefs = remember { UserPreferences(context) }
    val user by userPrefs.getUser().collectAsState(UserLocalModel("", "", "", ""))

    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var mobile by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val branches = listOf(
        "B.Tech – CSE","B.Tech – IT","B.Tech – ECE","B.Tech – EE",
        "B.Tech – ME","B.Tech – CE","M.Tech","MCA","MBA","BCA"
    )
    val years = (1972..2026).map { it.toString() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.hbtu),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF142338))
                ) {
                    Column(Modifier.padding(16.dp)) {

                        AppTextField("Name", name) { name = it }
                        AppTextField("Email", email) { email = it }

                        // Mobile
                        AppTextField(
                            label = "Mobile",
                            value = mobile,

                            onValueChange = {
                                if (it.all { c -> c.isDigit() } && it.length <= 10) mobile = it
                            }
                        )

                        Spacer(Modifier.height(10.dp))
                        DropdownField("Branch", branch, branches) { branch = it }
                        Spacer(Modifier.height(10.dp))
                        DropdownField("Year", year, years) { year = it }

                        AppTextField("Job / Company", job) { job = it }
                        AppTextField("Location", location) { location = it }

                        BirthdayPicker(birthday) { birthday = it }

                        AppTextField(
                            label = "LinkedIn URL",
                            value = linkedin,
                          //  keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),

                            onValueChange = { linkedin = it }
                        )

                        if (error != null) {
                            Text(error!!, color = Color.Red)
                        }

                        Button(
                            onClick = {
                                error = validateProfile(
                                    name, email, mobile, branch, year, job, location, birthday, linkedin
                                )
                                if (error == null) {
                                    navController.navigate("home") {
                                        popUpTo("profile") { inclusive = true }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Update Profile")
                        }
                    }
                }
            }
        }
    }
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
