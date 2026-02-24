package com.kwh.almuniconnect.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext

fun openWhatsApp(context: Context) {

    val phoneNumber = "9179057XXXX" // 🔥 FULL number without + and spaces
    val message = "Hello Alumni Support Team"

    val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"

    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        setPackage("com.whatsapp")
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavController) {
    TrackScreen("help_support_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Help & Support",
                navController = navController
            )
        },

    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {

            item { SupportHeader() }

            item { ContactSupportSection() }
        }
    }
}
@Composable
fun SupportHeader() {
    Text(
        text = "We’re here to help you connect better with your alumni network.",
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ContactSupportSection() {

    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Contact Support",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(12.dp))

        SupportAction(
            icon = Icons.Default.Email,
            title = "Email Support",
            value = "kabadiwalahub@gmail.com"
        )

        SupportAction(
            icon = Icons.Default.Whatsapp,
            title = "WhatsApp Support",
            value = "Tap to chat",
            iconTint = Color(0xFF25D366), // 💚 WhatsApp Green
            onClick = {
                openWhatsApp(context)
            }
        )


    }
}
@Composable
fun SupportAction(
    icon: ImageVector,
    title: String,
    value: String,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .then(
                if (onClick != null)
                    Modifier.clickable { onClick() }
                else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
