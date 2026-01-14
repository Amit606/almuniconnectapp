package com.kwh.almuniconnect.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.network.AlumniProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniProfileScreen(
    alumni: AlumniProfile,
    navController: NavController
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Profile ",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
              //  .background(Color(0xFF0E1420))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔵 Profile Image
            AsyncImage(
                model = alumni.imageUrl,
                contentDescription = "Profile photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 👤 Name
            Text(alumni.name, fontSize = 22.sp,  style = MaterialTheme.typography.titleMedium)

            // 💼 Position + Company
            Text(
                text = "${alumni.position} @ ${alumni.company}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 🎓 Branch + Year
            Text(
                text = "${alumni.branch} • Batch of ${alumni.passingYear}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 📞 Contact Card
            Card(
                modifier = Modifier.fillMaxWidth(),
             //   colors = CardDefaults.cardColors(containerColor = Color(0xFF142338)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    ProfileRow(
                        icon = Icons.Default.Phone,
                        label = "Mobile",
                        value = alumni.phone,
                        onClick = { callPhone(context, alumni.phone) }
                    )

                    ProfileRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = alumni.email,
                        onClick = { sendEmail(context, alumni.email) }
                    )

                    ProfileRow(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = alumni.location,
                        onClick = { openLocation(context, alumni.location) }
                    )

                    ProfileRow(
                        icon = Icons.Default.Whatsapp,
                        label = "Whats-up",
                        value = alumni.phone,
                        onClick = { openLocation(context, alumni.location) }
                    )

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔗 LinkedIn Button
            Button(
                onClick = {
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(alumni.profileUrl))
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Linked Profile")
            }
        }
    }
}

@Composable
fun ProfileRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(label,  style = MaterialTheme.typography.titleMedium)
            Text(value,  style = MaterialTheme.typography.titleMedium)
        }
    }
}

fun callPhone(context: Context, phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
}

fun sendEmail(context: Context, email: String) {
    val intent = Intent(
        Intent.ACTION_SENDTO,
        Uri.parse("mailto:$email")
    )
    context.startActivity(intent)
}

fun openLocation(context: Context, location: String) {
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(location)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
}