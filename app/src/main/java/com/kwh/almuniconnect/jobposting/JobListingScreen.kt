package com.kwh.almuniconnect.jobposting

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun JobListingScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Header
        item {
            Text(
                text = "Find a job",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))
            SearchRow()

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Promoted jobs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(12.dp))
            PromotedJobCard()

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Because you are interested in HR",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(12.dp))
        }

        // 🔥 20 Job Cards
        items(20) { index ->
            JobCard()
        }
    }
}

@Composable
fun SearchRow() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Enter job title or keyword") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(52.dp)
                .background(Color.Black, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Tune,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun PromotedJobCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6A80E8)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    JobIcon()
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "People partner",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Happy Company Co",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }

                Icon(
                    Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(12.dp))

            JobMetaRow(
                level = "Senior level",
                salary = "260K / year",
                isWhite = true
            )
        }
    }
}
@Composable
fun JobCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    JobIcon()
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            "HR Consultant",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "People Company Ltd",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                Icon(
                    Icons.Default.BookmarkBorder,
                    contentDescription = null
                )
            }

            Spacer(Modifier.height(12.dp))

            JobMetaRow(
                level = "Mid level",
                salary = "240K / year",
                isWhite = false
            )
        }
    }
}
@Composable
fun JobMetaRow(
    level: String,
    salary: String,
    isWhite: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            JobChip("Full-time")
            JobChip(level)
        }

        Text(
            text = "Source",
            color = if (isWhite) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}
@Composable
fun JobChip(text: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFFF1F5F9)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp
        )
    }
}

@Composable
fun JobIcon() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color(0xFFEFF6FF), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.Person,
            contentDescription = null
        )
    }
}
