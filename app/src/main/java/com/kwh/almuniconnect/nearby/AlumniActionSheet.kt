package com.kwh.almuniconnect.nearby

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.profile.callPhone
import com.kwh.almuniconnect.profile.openWhatsApp
import com.kwh.almuniconnect.profile.sendEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniActionSheet(
    alumni: NearAlumni,
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

            ActionItem("📞 Call") {
                callPhone(context, alumni.userId) // adjust mobile
            }

            ActionItem("💬 WhatsApp") {
                openWhatsApp(context, alumni.userId)
            }

            ActionItem("📧 Email") {
                sendEmail(context, "example@gmail.com")
            }

            ActionItem("👤 View Profile") {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("alumni", alumni)

                navController.navigate("profile")
                onDismiss()
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