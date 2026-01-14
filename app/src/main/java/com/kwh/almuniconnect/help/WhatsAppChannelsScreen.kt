package com.kwh.almuniconnect.help
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Groups
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import com.kwh.almuniconnect.R
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.kwh.almuniconnect.appbar.HBTUTopBar

data class WhatsAppChannel(
    val title: String,
    val description: String,
    val link: String
)

val channels = listOf(
    WhatsAppChannel(
        "Harcourtian MCA Alumni Official",
        "Important alumni announcements and meet updates",
        "https://chat.whatsapp.com/FooywyRBD1CDtnhHY5U2lh"
    ),
    WhatsAppChannel(
        "Harcourtian: Startup Accelerator",
        "Welcome to the HBTU Entrepreneurs’ Group - a space for alumni with ventures, ideas and ambitions",
        "https://whatsapp.com/channel/mca_jobs"
    ),
    WhatsAppChannel(
        "Harcourtian NCR",
        "All alumni events, reunions & seminars",
        "https://chat.whatsapp.com/KaA8C1V4Nnr1xE8mCQwaiv"
    ),
    WhatsAppChannel(
        "Harcourtian ALUMNI Gurgaon Chapter",
        "Students can connect with seniors for guidance",
        "https://whatsapp.com/channel/student_connect"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppChannelsScreen(navController: NavController) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Whats-up Community",
                navController = navController
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
            //    .background(Color(0xFF0E1420))
                .padding(paddingValues)
        ) {

            item {
                Text(
                    "Choose a channel to stay connected",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            items(channels) { channel ->
                WhatsAppChannelCard(channel, context)
            }
        }
    }
}
@Composable
fun WhatsAppChannelCard(channel: WhatsAppChannel, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channel.link))
                context.startActivity(intent)
            },
      //  colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2033)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_whatsup),
                contentDescription = "title",
                modifier = Modifier.size(60.dp),   // 🔥 120dp size
                        tint = Color.Unspecified   // keep original WhatsApp green
            )


            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(channel.title,  style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(channel.description,  style = MaterialTheme.typography.titleMedium)
            }

            Text("Join", style = MaterialTheme.typography.titleMedium)
        }
    }
}
