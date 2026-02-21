package com.kwh.almuniconnect.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.network.AlumniDto
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniProfileScreen(
    alumni: AlumniDto,
    navController: NavController
) {
    val context = LocalContext.current
    TrackScreen("alumni_profile_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Profile",
                navController = navController
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())   // ✅ ADD THIS
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔵 Profile Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(alumni.photoUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                placeholder = painterResource(R.drawable.man),
                error = painterResource(R.drawable.man),
                fallback = painterResource(R.drawable.man)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = alumni.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (alumni.isVerified) {
                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Alumni",
                        tint = Color(0xFF1DA1F2),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 💼 Position & Company
            Text(
                text = "${alumni.companyName} | ${alumni.totalExperience} yrs",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // 🎓 Branch & Year
            Text(
                text = "${alumni.courseName} | ${alumni.batch}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 📞 Contact Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    ProfileRow(
                        icon = Icons.Default.Phone,
                        label = "Mobile",
                        value = alumni.mobileNo.toString(),
                        onClick = { callPhone(context, alumni.mobileNo.toString()) }
                    )

                    Divider()

                    ProfileRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = alumni.email.toString(),
                        onClick = { sendEmail(context, alumni.email.toString()) }
                    )

                    Divider()

                    ProfileRow(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = alumni.countryName.toString(),
                        onClick = { openLocation(context, alumni.countryName.toString()) }
                    )
                    Log.e("AlumniProfileScreen", "Rendering WhatsApp row for number: ${alumni.countryName.toString()}")

                    Divider()

                    ProfileRow(
                        icon = Icons.Default.Whatsapp,
                        label = "WhatsApp",
                        value = alumni.mobileNo.toString(),
                        iconTint = Color(0xFF25D366),
                        onClick = { openWhatsApp(context, alumni.mobileNo.toString()) }
                    )
//                    Divider()
//
//                    ProfileRow(
//                        icon = Icons.Default.Countertops,
//                        label = "Job Referral ",
//                        value = "Job Posted 3",
//                        iconTint = Color(0xFF25D366),
//                        onClick = { openWhatsApp(context, alumni.mobileNo.toString()) }
//                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔗 LinkedIn Button
            OutlinedButton(
                onClick = {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            alumni.linkedinUrl.toString().toUri()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("View LinkedIn Profile")
            }
        }
    }
}

@Composable
fun ProfileRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    iconTint.copy(alpha = 0.12f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/* ---------------- ACTION HELPERS ---------------- */

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
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}

fun openWhatsApp(context: Context, phone: String) {
    val uri = Uri.parse("https://wa.me/$phone")
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}
