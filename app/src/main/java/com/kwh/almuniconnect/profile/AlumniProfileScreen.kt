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
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import com.kwh.almuniconnect.network.AlumniDto
import androidx.core.net.toUri
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas

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
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /* ---------------- PROFILE IMAGE ---------------- */

            HarcourtianProfileImage(alumni.photoUrl)

            Spacer(modifier = Modifier.height(20.dp))

            /* ---------------- NAME ---------------- */

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = alumni.name,
                    style = MaterialTheme.typography.headlineSmall,
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

            Spacer(modifier = Modifier.height(4.dp))

            /* ---------------- COMPANY ---------------- */

            Text(
                text = "${alumni.companyName} • ${alumni.totalExperience} yrs experience",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            /* ---------------- EDUCATION ---------------- */

            Text(
                text = "${alumni.courseName} • Batch ${alumni.batch}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------------- CONTACT TITLE ---------------- */

            Text(
                text = "Contact Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            /* ---------------- CONTACT CARD ---------------- */

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
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

                    HorizontalDivider()

                    ProfileRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = alumni.email.toString(),
                        onClick = { sendEmail(context, alumni.email.toString()) }
                    )

                    HorizontalDivider()

                    ProfileRow(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = alumni.countryName.toString(),
                        onClick = { openLocation(context, alumni.countryName.toString()) }
                    )

                    HorizontalDivider()

                    ProfileRow(
                        icon = Icons.Default.Whatsapp,
                        label = "WhatsApp",
                        value = alumni.mobileNo.toString(),
                        iconTint = Color(0xFF25D366),
                        onClick = { openWhatsApp(context, alumni.mobileNo.toString()) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            /* ---------------- LINKEDIN BUTTON ---------------- */

            OutlinedButton(
                onClick = {

                    try {

                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                alumni.linkedinUrl.toString().toUri()
                            )
                        )

                    } catch (e: Exception) {

                        Log.e(
                            "AlumniProfileScreen",
                            "Failed to open LinkedIn URL: ${alumni.linkedinUrl}",
                            e
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {

                Icon(Icons.Default.OpenInNew, contentDescription = null)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    "View LinkedIn Profile",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/* ---------------- PROFILE ROW ---------------- */

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
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    iconTint.copy(alpha = 0.10f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/* ---------------- HARCOURTIAN IMAGE ---------------- */

@Composable
fun HarcourtianProfileImage(photoUrl: String?) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(150.dp)
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoUrl ?: "")
                .crossfade(true)
                .build(),
            contentDescription = "Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.man),
            error = painterResource(R.drawable.man),
            fallback = painterResource(R.drawable.man)
        )

        Canvas(modifier = Modifier.size(150.dp)) {

            val strokeWidth = 10f

            drawCircle(
                color = Color(0xFF2E7D32),
                style = Stroke(width = strokeWidth)
            )

//            drawContext.canvas.nativeCanvas.apply {
//
//                val text = "I AM HARCOURTIAN"
//
//                val paint = Paint().apply {
//                    color = android.graphics.Color.WHITE
//                    textSize = 32f
//                    textAlign = Paint.Align.CENTER
//                    isFakeBoldText = true
//                }
//
//                val radius = size.minDimension / 2
//
//                val path = Path().apply {
//                    addCircle(
//                        size.width / 2,
//                        size.height / 2,
//                        radius - strokeWidth,
//                        Path.Direction.CW
//                    )
//                }
//
//                drawTextOnPath(
//                    text,
//                    path,
//                    0f,
//                    0f,
//                    paint
//                )
//            }
        }
    }
}

/* ---------------- ACTION HELPERS ---------------- */

fun callPhone(context: Context, phone: String) {

    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
}

fun sendEmail(context: Context, email: String) {

    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
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