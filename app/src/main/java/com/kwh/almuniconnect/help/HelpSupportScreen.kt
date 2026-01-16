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
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavController) {

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
            value = "support@alumniconnect.com"
        )

        SupportAction(
            icon = Icons.Default.Whatsapp,
            title = "WhatsApp Support",
            value = "+91 79xxxxxxxx0"
        )

        SupportAction(
            icon = Icons.Default.Web,
            title = "Visit Website",
            value = "www.alumniconnect.com"
        )
    }
}
@Composable
fun SupportAction(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
