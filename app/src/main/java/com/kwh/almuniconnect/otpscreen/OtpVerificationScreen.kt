package com.kwh.almuniconnect.otpscreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.login.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    navController : NavHostController,
    email: String,
    onBack: () -> Unit = {},
    onVerifyOtp: (String) -> Unit = {},
    onResendOtp: () -> Unit = {},
    viewModel: AuthViewModel = viewModel() // ✅ HERE

) {
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(16.dp))

            // 🔙 Back
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // 🏷 Title
            Text(
                text = "Verify OTP",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "We have sent a verification code to",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = email,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(Modifier.height(32.dp))

            // 🔢 OTP Input
            OtpInputField(
                otp = otp,
                onOtpChange = {
                    if (it.length <= 6 && it.all { ch -> ch.isDigit() }) {
                        otp = it
                    }
                }
            )

            Spacer(Modifier.height(32.dp))

            // 🔵 Verify Button
            Button(
                onClick = {
                    viewModel.verifyOtp(
                        otp = otp,
                        onSuccess = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        },
                        onError = { error ->
                            errorMessage = error
                        }
                    )
                },
                enabled = otp.length == 6
            ) {
                Text("Verify OTP")
            }

            Spacer(Modifier.height(20.dp))

            // ⏱ Resend
            Text(
                text = "Didn’t receive OTP?",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Resend OTP",
                fontSize = 14.sp,
                color = Color(0xFF5A67D8),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onResendOtp() }
            )
        }
    }
}

@Composable
fun OtpInputField(
    otp: String,
    onOtpChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(6) { index ->
            val char = otp.getOrNull(index)?.toString() ?: ""

            OutlinedTextField(
                value = char,
                onValueChange = { value ->
                    if (value.length <= 1) {
                        val newOtp = StringBuilder(otp)
                        if (otp.length > index) {
                            newOtp.setCharAt(index, value.firstOrNull() ?: ' ')
                        } else {
                            newOtp.append(value)
                        }
                        onOtpChange(newOtp.toString().trim())
                    }
                },
                modifier = Modifier
                    .width(48.dp)
                    .height(56.dp),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF5A67D8),
                    unfocusedBorderColor = Color.LightGray
                )
            )
        }
    }
}
