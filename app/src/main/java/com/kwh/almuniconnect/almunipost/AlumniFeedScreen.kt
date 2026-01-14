package com.kwh.almuniconnect.almunipost
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlumniFeedScreen(
    navController: NavController,
    onNewPostClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Alumni Feeds",
                navController = navController
            )
        }
    ) { paddingValues ->

        Column(
                 modifier = Modifier.fillMaxWidth()
                     .background(Color(0xFF0E1420))
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {



            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(alumniFeed) { post ->
                    AlumniPostCard(post)
                }
            }
        }
    }
}
@Composable
fun SectionTitle(
    title: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (actionText != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(actionText)
            }
        }
    }
}
@Composable
fun AlumniPostCard(post: AlumniPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF142338)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = post.name,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = post.batch,
                fontSize = 12.sp,
                color = Color.White,

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = post.message,  color = Color.White,
            )
        }
    }
}
