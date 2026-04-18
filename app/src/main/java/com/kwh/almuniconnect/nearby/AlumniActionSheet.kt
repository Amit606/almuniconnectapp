package com.kwh.almuniconnect.nearby

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.profile.callPhone
import com.kwh.almuniconnect.profile.openWhatsApp
import com.kwh.almuniconnect.profile.sendEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniActionSheet(
    alumni: NearAlumni,
    isPremium: Boolean, // 🔥 ADD THIS
    onDismiss: () -> Unit,
    navController: NavController
) {

    val context = LocalContext.current

    ModalBottomSheet(onDismissRequest = onDismiss) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = alumni.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔓 FREE ACTION
            ActionItem("👤 View Profile") {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("alumni", alumni)

                navController.navigate("profile")
                onDismiss()
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🔒 LOCKED ACTIONS
            PremiumActionItem(
                title = "📞 Call",
                isPremium = isPremium,
                onClick = {
                    callPhone(context, alumni.mobileNo ?: "")
                },
                navController = navController
            )

            PremiumActionItem(
                title = "💬 WhatsApp",
                isPremium = isPremium,
                onClick = {
                    openWhatsApp(context, alumni.mobileNo ?: "")
                },
                navController = navController
            )

            PremiumActionItem(
                title = "📧 Email",
                isPremium = isPremium,
                onClick = {
                    sendEmail(context, alumni.email ?: "")
                },
                navController = navController
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!isPremium) {
                Text(
                    text = "🔒 Unlock all contact details with Premium",
                    color = Color(0xFF1E88E5),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
@Composable
fun ActionItem(title: String, onClick: () -> Unit) {

    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        fontSize = 16.sp
    )
}

@Composable
fun PremiumActionItem(
    title: String,
    isPremium: Boolean,
    onClick: () -> Unit,
    navController: NavController
) {

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                if (isPremium) {
                    onClick()
                } else {
                    Toast
                        .makeText(context, "Unlock Premium to continue", Toast.LENGTH_SHORT)
                        .show()

                    navController.navigate(Routes.SUBSCRIPTION)
                }
            }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        if (!isPremium) {
            Text("🔒", fontSize = 16.sp)
        } else {
            Text("⭐", fontSize = 16.sp)
        }
    }
}