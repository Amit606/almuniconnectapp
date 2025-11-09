import android.app.DatePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import java.util.Calendar
import java.util.Locale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
data class RegistrationData(
    val name: String = "",
    val mobile: String = "",
    val email: String = "",
    val branch: String = "",
    val passingYear: String = "",
    val jobDetails: String = "",
    val linkedIn: String = "",
    val dob: String = "",
    val anniversary: String = "",
    val profileUri: Uri? = null
)
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

    // Branch dropdown state
    var branchExpanded by remember { mutableStateOf(false) }
    val branches = listOf("Computer Science", "Mechanical", "Electrical", "Civil", "IT", "ECE")
    var selectedBranch by rememberSaveable { mutableStateOf(branches.first()) }

    // Passing year dropdown (1925..2026)
    var yearExpanded by remember { mutableStateOf(false) }
    val years = (1925..2026).map { it.toString() }.reversed() // latest first
    var selectedYear by rememberSaveable { mutableStateOf(years.first()) }

    var jobDetails by rememberSaveable { mutableStateOf("") }
    var linkedIn by rememberSaveable { mutableStateOf("") }

    var dobText by rememberSaveable { mutableStateOf("") }
    var anniversaryText by rememberSaveable { mutableStateOf("") }

    var profileUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    // Image picker
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        profileUri = uri
    }

    // Calendar helpers for DatePicker min/max
    val minCal = Calendar.getInstance().apply { set(1925, 0, 1, 0, 0, 0); set(Calendar.MILLISECOND, 0) }
    val maxCal = Calendar.getInstance().apply { set(2026, 11, 31, 23, 59, 59); set(Calendar.MILLISECOND, 999) }

    // Show date picker with min/max so user can scroll years 1925..2026
    fun showDatePicker(initialDate: Calendar?, onDateSelected: (String) -> Unit) {
        val startCal = initialDate ?: Calendar.getInstance()
        val dialog = DatePickerDialog(
            context,
            { _, year, month, day ->
                val m = month + 1
                onDateSelected(String.format(Locale.getDefault(), "%02d/%02d/%04d", day, m, year))
            },
            startCal.get(Calendar.YEAR),
            startCal.get(Calendar.MONTH),
            startCal.get(Calendar.DAY_OF_MONTH)
        )
        // apply limits
        dialog.datePicker.minDate = minCal.timeInMillis
        dialog.datePicker.maxDate = maxCal.timeInMillis
        dialog.show()
    }

    // validation state
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0E1420)) // ✅ Set your background color here


            // important: add status bar insets here as part of modifier chain
            .windowInsetsPadding(WindowInsets.statusBars)
            // allow vertical scroll so fields are accessible on small screens / with keyboard
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create Account", color=Color.White, style = MaterialTheme.typography.headlineSmall, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(12.dp))

        // Profile image
        Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
            if (profileUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileUri),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { imagePicker.launch("image/*") },
                    contentScale = ContentScale.Crop
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

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name",color=Color.White,) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
           // keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mobile
        OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it.filter { ch -> ch.isDigit() }.take(10) },
            label = { Text("Mobile Number",color=Color.White,) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
           // keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email",color=Color.White,) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
          //  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Branch dropdown (ExposedDropdownMenuBox)
        ExposedDropdownMenuBox(
            expanded = branchExpanded,
            onExpandedChange = { branchExpanded = !branchExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedBranch,
                onValueChange = { },
                readOnly = true,
                label = { Text("Branch",color=Color.White,) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = branchExpanded) },
                modifier = Modifier
                    .menuAnchor() // anchors menu to this text field
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = branchExpanded,
                onDismissRequest = { branchExpanded = false }
            ) {
                branches.forEach { selection ->
                    DropdownMenuItem(
                        text = { Text(selection,color=Color.White) },
                        onClick = {
                            selectedBranch = selection
                            branchExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Passing year dropdown
        ExposedDropdownMenuBox(
            expanded = yearExpanded,
            onExpandedChange = { yearExpanded = !yearExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedYear,
                onValueChange = { },
                readOnly = true,
                label = { Text("Passing Year",color=Color.White,) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = yearExpanded,
                onDismissRequest = { yearExpanded = false }
            ) {
                years.forEach { y ->
                    DropdownMenuItem(
                        text = { Text(y,color=Color.White,) },
                        onClick = {
                            selectedYear = y
                            yearExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Job details
        OutlinedTextField(
            value = jobDetails,
            onValueChange = { jobDetails = it },
            label = { Text("Job Details (Company / Role)",color=Color.White,) },
            modifier = Modifier.fillMaxWidth(),
           // keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LinkedIn
        OutlinedTextField(
            value = linkedIn,
            onValueChange = { linkedIn = it },
            label = { Text("LinkedIn URL",color=Color.White,) },
            modifier = Modifier.fillMaxWidth(),
          //  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // DOB and Anniversary row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = dobText,
                onValueChange = { dobText = it },
                label = { Text("Date of Birth",color=Color.White,) },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker(null) { dobText = it } },
                readOnly = true
            )

            OutlinedTextField(
                value = anniversaryText,
                onValueChange = { anniversaryText = it },
                label = { Text("Anniversary",color=Color.White,) },
                modifier = Modifier
                    .weight(1f)
                    .clickable { showDatePicker(null) { anniversaryText = it } },
                readOnly = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showError) {
            Text(text = "Please fill all required fields correctly", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                // Basic validation
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
                        dob = dobText,
                        anniversary = anniversaryText,
                        profileUri = profileUri
                    )
                    onRegister(data)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF142338)
            ),
            shape = RoundedCornerShape(12.dp)

        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
