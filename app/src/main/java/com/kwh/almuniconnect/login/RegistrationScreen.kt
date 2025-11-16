// RegistrationScreen.kt
package com.kwh.almuniconnect.login

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onRegister: (RegistrationData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var mobile by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var dob by rememberSaveable { mutableStateOf("") }                // yyyy-MM-dd
    var anniversary by rememberSaveable { mutableStateOf("") }       // yyyy-MM-dd

    val branchList = listOf("Computer Science", "Mechanical", "Electrical", "Civil", "IT", "ECE")
    var branchExpanded by remember { mutableStateOf(false) }
    var selectedBranch by rememberSaveable { mutableStateOf(branchList.first()) }

    var yearExpanded by remember { mutableStateOf(false) }
    val years = (1975..2026).map { it.toString() }.reversed()
    var selectedYear by rememberSaveable { mutableStateOf(years.first()) }

    var jobDetails by rememberSaveable { mutableStateOf("") }
    var linkedIn by rememberSaveable { mutableStateOf("") }

    var profileUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> profileUri = uri }

    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0E1420))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", color = Color.White, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
            if (profileUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileUri),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { imagePicker.launch("image/*") }
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(android.R.drawable.ic_menu_camera), contentDescription = "Add")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it },
            textStyle = LocalTextStyle.current.copy(color = Color.White)
                ,label = { Text("Full Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = mobile,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            textStyle = LocalTextStyle.current.copy(color = Color.White)
,           onValueChange = { mobile = it.filter { ch -> ch.isDigit() }.take(10) },
            label = { Text("Mobile Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = email,
            textStyle = LocalTextStyle.current.copy(color = Color.White),
                    onValueChange = { email = it }, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // Branch dropdown
        ExposedDropdownMenuBox(expanded = branchExpanded, onExpandedChange = { branchExpanded = !branchExpanded }) {
            OutlinedTextField(
                value = selectedBranch,
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(color = Color.White),

                        label = { Text("Branch") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = branchExpanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = branchExpanded, onDismissRequest = { branchExpanded = false }) {
                branchList.forEach { b ->
                    DropdownMenuItem(text = { Text(b) }, onClick = { selectedBranch = b; branchExpanded = false })
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Passing year
        ExposedDropdownMenuBox(expanded = yearExpanded, onExpandedChange = { yearExpanded = !yearExpanded }) {
            OutlinedTextField(
                value = selectedYear,
                onValueChange = {},
                readOnly = true,
                label = { Text("Passing Year") },
                textStyle = LocalTextStyle.current.copy(color = Color.White),

                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = yearExpanded, onDismissRequest = { yearExpanded = false }) {
                years.forEach { y ->
                    DropdownMenuItem(text = { Text(y) }, onClick = { selectedYear = y; yearExpanded = false })
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = jobDetails,
            textStyle = LocalTextStyle.current.copy(color = Color.White),


                    onValueChange = { jobDetails = it }, label = { Text("Job Details (Company / Role)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = linkedIn,
            textStyle = LocalTextStyle.current.copy(color = Color.White),


            onValueChange = { linkedIn = it }, label = { Text("LinkedIn URL") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))

        // DOB + Anniversary row with equal weights
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DatePickerField(label = "Date of Birth", selectedDate = dob, onDateSelected = { dob = it } )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DatePickerField(label = "Anniversary", selectedDate = anniversary, onDateSelected = { anniversary = it })
        }


        Spacer(Modifier.height(16.dp))

        if (showError) {
            Text("Please fill all required fields correctly", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                showError = name.isBlank() || mobile.length < 10 || email.isBlank()
                if (!showError) {
                    val data = RegistrationData(
                        name = name,
                        mobile = mobile,
                        email = email,
                        branch = selectedBranch,
                        passingYear = selectedYear,
                        jobDetails = jobDetails,
                        linkedIn = linkedIn,
                        dob = dob,
                        anniversary = anniversary,
                        profileUri = profileUri
                    )
                    onRegister(data)
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Register")
        }
        Spacer(Modifier.height(24.dp))
    }
}
