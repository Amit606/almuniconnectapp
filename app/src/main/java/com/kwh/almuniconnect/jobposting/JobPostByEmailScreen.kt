package com.kwh.almuniconnect.jobposting
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostByEmailScreen(
    navController: NavController
) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Post Opportunity",
                navController = navController
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            /* ---------- HERO CARD ---------- */

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFF6FF)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "🤝 Help a Fellow Harcourtian Grow",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Share job opportunities from your company or network.\nYour small effort can change someone’s career.",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------- BENEFITS ---------- */

            Column {

                BenefitItem("🎯 Verified job posting")
                BenefitItem("🚀 Reach 500+ alumni instantly")
                BenefitItem("💼 Help someone grow in career")

            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- CTA BUTTON ---------- */

            Button(
                onClick = {

                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:".toUri()

                        putExtra(Intent.EXTRA_EMAIL, arrayOf("support@appschance.com"))
                        putExtra(Intent.EXTRA_BCC, arrayOf("amitsun.noida@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Job Opportunity from Alumni")

                        putExtra(
                            Intent.EXTRA_TEXT,
                            """
Job Opportunity Details:

Company:
Role:
Location:
Experience:
Apply Link:

Shared via Alumni Connect App
                            """.trimIndent()
                        )
                    }

                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("📩 Share Job via Email", fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            /* ---------- TRUST LINE ---------- */

            Text(
                text = "Your submission will be verified before publishing",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun BenefitItem(text: String) {

    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = text,
            fontSize = 13.sp,
            color = Color.Black
        )
    }
}

@Composable
fun SectionHeader(text: String) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = text,
        color=Color.Black,
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(8.dp))
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    errorText: String? = null,
    singleLine: Boolean = true,
    readOnly: Boolean = false,        // ✅ NEW
    enabled: Boolean = true,          // ✅ NEW
    onValueChange: (String) -> Unit
) {

    Column(modifier = modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            readOnly = readOnly,        // ✅ APPLY
            enabled = enabled,          // ✅ APPLY
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp
                )
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = if (enabled) Color.Black else Color.Gray
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledBorderColor = Color(0xFFE0E0E0)
            ),
            isError = isError
        )

        if (isError && errorText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }
    }
}




@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    errorText: String = "",
    maxLines: Int = 1
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines,
            isError = isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            )
        )

        if (isError) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

private fun isValidUrl(url: String): Boolean {
    if (url.isBlank()) return true
    return android.util.Patterns.WEB_URL.matcher(url).matches()
}


@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = trailingContent
    )
}




@Composable
fun LinkiedID(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    val context = LocalContext.current

    // 🔥 Your LinkedIn URL
    val linkedInUrl = "https://www.linkedin.com/in/"

    Column(modifier = Modifier.fillMaxWidth()) {

        // ✅ Only Input Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Clickable LinkedIn URL Below
        Text(
            text = linkedInUrl,
            color = Color(0xFF0A66C2), // LinkedIn blue
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
                context.startActivity(intent)
            }
        )
    }
}