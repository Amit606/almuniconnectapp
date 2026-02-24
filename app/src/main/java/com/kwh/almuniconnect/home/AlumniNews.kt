package com.kwh.almuniconnect.home
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.almunipost.AlumniStory
import com.kwh.almuniconnect.jobposting.JobAPost
@Composable
fun AlumniNews(
    post: UniversityNews,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column {

            // 🔹 Top Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.imageUrl?.takeIf { it.isNotBlank() })
                    .crossfade(true)
                    .placeholder(R.drawable.ic_news)
                    .error(R.drawable.ic_news)
                    .fallback(R.drawable.ic_news)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // 🔹 Category + Date Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = post.category ?: "News",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8E8E93)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "•",
                        color = Color(0xFF8E8E93)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = post.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8E8E93)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Title
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1E),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 🔹 Author + Arrow Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {

                        AsyncImage(
                            model = post.authorImage,
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape),
                            error = painterResource(R.drawable.man)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = post.authorName ?: "Alumni",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1C1C1E)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color(0xFF8E8E93)
                    )
                }
            }
        }
    }
}