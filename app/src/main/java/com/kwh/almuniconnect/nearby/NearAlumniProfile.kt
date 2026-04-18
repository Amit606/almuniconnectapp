package com.kwh.almuniconnect.nearby
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.core.net.toUri
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.billing.BillingViewModel
import com.kwh.almuniconnect.profile.HarcourtianProfileImage
import com.kwh.almuniconnect.profile.PremiumProfileRow
import com.kwh.almuniconnect.profile.callPhone
import com.kwh.almuniconnect.profile.openLocation
import com.kwh.almuniconnect.profile.openWhatsApp
import com.kwh.almuniconnect.profile.sendEmail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearAlumniProfile(
    alumni: NearAlumni,
    navController: NavController
) {

    val context = LocalContext.current
    TrackScreen("alumni_profile_screen_v2")

    val billingViewModel: BillingViewModel = viewModel()
    val isPremium = billingViewModel.isPremium.collectAsState().value

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

            HarcourtianProfileImage(alumni.profileImage)

            Spacer(modifier = Modifier.height(20.dp))

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

            Text(
                text = "${alumni.companyName} • ${alumni.totalExperience} yrs experience",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "${alumni.courseId} • Batch ${alumni.batch}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Contact Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {

                Column(modifier = Modifier.padding(16.dp)) {

                    PremiumProfileRow(
                        icon = Icons.Default.Phone,
                        label = "Mobile",
                        value = alumni.mobileNo.toString(),
                        isPremium = isPremium,
                        onUnlockClick = {
                            navController.navigate(Routes.SUBSCRIPTION)
                        },
                        onClick = {
                            callPhone(context, alumni.mobileNo.toString())
                        }
                    )

                    HorizontalDivider()

                    PremiumProfileRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = alumni.email.toString(),
                        isPremium = isPremium,
                        onUnlockClick = {
                            navController.navigate(Routes.SUBSCRIPTION)
                        },
                        onClick = {
                            sendEmail(context, alumni.email.toString())
                        }
                    )

                    HorizontalDivider()

                    PremiumProfileRow(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = alumni.countryName.toString(),
                        isPremium = isPremium,
                        onUnlockClick = {
                            navController.navigate(Routes.SUBSCRIPTION)
                        },
                        onClick = {
                            openLocation(context, alumni.countryName.toString())
                        }
                    )

                    HorizontalDivider()

                    PremiumProfileRow(
                        icon = Icons.Default.Whatsapp,
                        label = "WhatsApp",
                        value = alumni.mobileNo.toString(),
                        isPremium = isPremium,
                        onUnlockClick = {
                            navController.navigate(Routes.SUBSCRIPTION)
                        },
                        onClick = {
                            openWhatsApp(context, alumni.mobileNo.toString())
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

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
                        Log.e("AlumniProfileScreenV2", "LinkedIn open failed", e)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {

                Icon(Icons.Default.OpenInNew, contentDescription = null)

                Spacer(modifier = Modifier.width(8.dp))

                Text("View LinkedIn Profile")
            }
        }
    }
}