package com.kwh.almuniconnect.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Privacy Policy",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp)   // Clean & balanced padding
                .verticalScroll(rememberScrollState())
        ) {
            // Main Title
            Text(
                text = "Privacy Policy",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Effective Date: 01 January 2026",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            // Security Highlight Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🔒 Your Privacy Matters",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "AlumniConnect respects your privacy and is committed to protecting your personal information.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Policy Sections
            PolicySection(
                title = "1. Information We Collect",
                content = "• Personal Information (Name, Email Address, Phone Number)\n" +
                        "• Profile Information (Branch, Passing Year, Company, Location)\n" +
                        "• Authentication Data via Google Sign-In\n" +
                        "• App Usage & Analytics Data\n" +
                        "• Device Information (Device ID, OS version)"
            )

            PolicySection(
                title = "2. How We Use Your Information",
                content = "• To create and manage user accounts\n" +
                        "• To provide alumni networking features\n" +
                        "• To send notifications and updates\n" +
                        "• To improve app performance and security"
            )

            PolicySection(
                title = "3. Firebase Services",
                content = "We use Firebase services by Google for authentication, secure cloud storage, analytics, crash reporting, and push notifications.\n\n" +
                        "All data transmitted between the app and Firebase servers is encrypted in transit using HTTPS."
            )

            PolicySection(
                title = "4. Google Sign-In",
                content = "We allow users to sign in using their Google account. We only access:\n" +
                        "• Name\n" +
                        "• Email address\n" +
                        "• Profile picture\n\n" +
                        "We never access your Google password or any private Google data."
            )

            PolicySection(
                title = "5. AdMob (Advertising)",
                content = "We use Google AdMob to display advertisements. AdMob may collect Advertising ID and device information for ad personalization.\n\n" +
                        "Users can manage ad preferences through Google Ad Settings."
            )

            PolicySection(
                title = "6. In-App Purchases",
                content = "All in-app purchases are processed securely through Google Play Billing. We do not collect or store payment card details."
            )

            PolicySection(
                title = "7. Data Sharing",
                content = "We do not sell or rent personal data.\n\n" +
                        "Data may be shared only with:\n" +
                        "• Google services (Firebase, AdMob, Play Billing)\n" +
                        "• Legal authorities if required by law"
            )

            PolicySection(
                title = "8. Data Retention",
                content = "We retain user data as long as the account remains active. If a user deletes their account, personal data is permanently removed within 7 days (except where legal obligations require longer retention)."
            )

            PolicySection(
                title = "9. Account & Data Deletion",
                content = "Users can delete their account directly inside the app from the Settings section.\n\n" +
                        "If unable to access the app, users may request deletion by emailing:\n" +
                        "support@appschance.com\n\n" +
                        "All deletion requests are processed within 7 working days."
            )

            PolicySection(
                title = "10. Data Security",
                content = "We implement industry-standard security measures including encrypted data transmission (HTTPS) and secure cloud storage to protect user information."
            )

            PolicySection(
                title = "11. International Data Transfers",
                content = "Since we use Google Firebase services, data may be processed on servers located outside your country. Google ensures appropriate data protection safeguards."
            )

            PolicySection(
                title = "12. Children’s Privacy",
                content = "AlumniConnect is not intended for children under 13. We do not knowingly collect personal data from children."
            )

            PolicySection(
                title = "13. Third-Party Links",
                content = "Our app may contain links to third-party services. We are not responsible for the privacy practices of those services."
            )

            PolicySection(
                title = "14. Changes to This Policy",
                content = "We may update this Privacy Policy periodically. Changes will be posted on this page with an updated effective date."
            )

            PolicySection(
                title = "15. Contact Us",
                content = "If you have any questions about this Privacy Policy, contact us:\n\n" +
                        "📧 Email: support@appschance.com"
            )

            Spacer(Modifier.height(40.dp))

            Text(
                text = "© 2026 AlumniConnect. All rights reserved.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PolicySection(
    title: String,
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = content,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}