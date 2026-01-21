package com.kwh.almuniconnect.branding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.compose.foundation.lazy.items


@Composable
fun ProductServiceDummyScreen(
    navController: NavController
) {
    val context = LocalContext.current

    TrackScreen("product_details_screen")

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Service Details",
                navController = navController
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // ✅ IMPORTANT
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dummyProductServices) { item ->
                ProductServiceCard(
                    item = item)

            }
        }
    }
}
@Composable
fun ProductServiceCard(
    item: ProductServiceItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {

            // 🖼️ Image (Alumni photo / fallback)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.photoUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.newggg)
                    .error(R.drawable.newggg)
                    .fallback(R.drawable.newggg)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {

                TypeChip(item.type)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(12.dp))

                AlumniFooter(item)
            }
        }
    }
}
@Composable
fun TypeChip(type: String) {
    val bgColor = if (type.equals("Product", true))
        Color(0xFFE3F2FD)
    else
        Color(0xFFE8F5E9)

    val textColor = if (type.equals("Product", true))
        Color(0xFF1565C0)
    else
        Color(0xFF2E7D32)

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = type,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}
@Composable
fun AlumniFooter(item: ProductServiceItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.alumniName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${item.courseName} • Batch ${item.batch}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        Text(
            text = "View",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}
val dummyProductServices = listOf(

    ProductServiceItem(
        productId = "1",
        type = "Product",
        title = "Clinical Trial Management System",
        description = "A comprehensive platform to manage clinical trial workflows, patient data, and regulatory compliance.",
        link = "https://example.com/products/ctms",
        alumniId = "A1",
        alumniName = "Ravi Mishra",
        courseName = "MCA",
        batch = "2013",
        photoUrl = "https://randomuser.me/api/portraits/men/32.jpg",
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 1
    ),

    ProductServiceItem(
        productId = "2",
        type = "Service",
        title = "Healthcare Data Analytics",
        description = "Advanced analytics services to derive insights from healthcare and clinical trial data.",
        link = "https://example.com/services/healthcare-analytics",
        alumniId = "A2",
        alumniName = "Anjali Gupta",
        courseName = "MCA",
        batch = "1980",
        photoUrl = null,
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 2
    ),

    ProductServiceItem(
        productId = "3",
        type = "Product",
        title = "Patient Engagement Mobile App",
        description = "A mobile application designed to improve patient engagement and adherence during clinical studies.",
        link = "https://example.com/products/patient-app",
        alumniId = "A3",
        alumniName = "Vinod Gupta",
        courseName = "MCA",
        batch = "1981",
        photoUrl = "https://randomuser.me/api/portraits/men/45.jpg",
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 3
    ),

    ProductServiceItem(
        productId = "4",
        type = "Service",
        title = "Regulatory Compliance Consulting",
        description = "Expert consulting services for regulatory submissions, audits, and compliance in clinical research.",
        link = "https://example.com/services/regulatory-compliance",
        alumniId = "A4",
        alumniName = "Akshita Mishra",
        courseName = "MCA",
        batch = "1980",
        photoUrl = null,
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 4
    ),

    ProductServiceItem(
        productId = "5",
        type = "Product",
        title = "Legacy Reporting Tool",
        description = "An older reporting tool for generating trial reports, now replaced by modern analytics solutions.",
        link = "https://example.com/products/legacy-reporting",
        alumniId = "A5",
        alumniName = "Anjali Gupta",
        courseName = "MCA",
        batch = "1982",
        photoUrl = "https://randomuser.me/api/portraits/women/65.jpg",
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 5
    ),

    ProductServiceItem(
        productId = "6",
        type = "Service",
        title = "AI Consulting for Startups",
        description = "End-to-end AI consulting services including model development, deployment, and scaling.",
        link = "https://example.com/services/ai-consulting",
        alumniId = "A6",
        alumniName = "Saurabh Verma",
        courseName = "B.Tech",
        batch = "2015",
        photoUrl = "https://randomuser.me/api/portraits/men/12.jpg",
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 6
    ),

    ProductServiceItem(
        productId = "7",
        type = "Product",
        title = "Campus ERP Solution",
        description = "An integrated ERP platform for managing academics, finance, and administration.",
        link = "https://example.com/products/campus-erp",
        alumniId = "A7",
        alumniName = "Neha Sharma",
        courseName = "MCA",
        batch = "2012",
        photoUrl = null,
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 7
    ),

    ProductServiceItem(
        productId = "8",
        type = "Service",
        title = "Cybersecurity Audit Services",
        description = "Professional cybersecurity audits, penetration testing, and risk assessment services.",
        link = "https://example.com/services/cybersecurity",
        alumniId = "A8",
        alumniName = "Amit Singh",
        courseName = "B.Tech",
        batch = "2009",
        photoUrl = "https://randomuser.me/api/portraits/men/78.jpg",
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 8
    ),

    ProductServiceItem(
        productId = "9",
        type = "Product",
        title = "E-Learning Management Platform",
        description = "A scalable LMS platform offering courses, assessments, and live classes.",
        link = "https://example.com/products/lms",
        alumniId = "A9",
        alumniName = "Pooja Malhotra",
        courseName = "MBA",
        batch = "2016",
        photoUrl = "https://randomuser.me/api/portraits/women/12.jpg",
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 9
    ),

    ProductServiceItem(
        productId = "10",
        type = "Service",
        title = "Startup Mentorship Program",
        description = "One-on-one mentorship and growth strategy guidance for early-stage startups.",
        link = "https://example.com/services/startup-mentorship",
        alumniId = "A10",
        alumniName = "Rahul Khanna",
        courseName = "B.Tech",
        batch = "2005",
        photoUrl = null,
        isActive = true,
        createdAtUtc = "2026-01-19T08:52:20.668",
        srNo = 10
    )
)
