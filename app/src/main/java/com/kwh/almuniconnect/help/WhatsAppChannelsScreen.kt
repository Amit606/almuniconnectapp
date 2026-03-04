package com.kwh.almuniconnect.help
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import com.kwh.almuniconnect.R
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.style.TextOverflow
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.appbar.HBTUTopBar
import org.json.JSONArray


/* ------------------------------------------------ */
/* SOCIAL TYPE ENUM */
/* ------------------------------------------------ */

enum class SocialType {
    WHATSAPP,
    FACEBOOK,
    INSTAGRAM,
    TWITTER,
    YOUTUBE
}

/* ------------------------------------------------ */
/* DATA MODEL */
/* ------------------------------------------------ */

data class SocialAppChannel(
    val type: SocialType,
    val title: String,
    val description: String,
    val link: String
)

/* ------------------------------------------------ */
/* CHANNEL LIST */
/* ------------------------------------------------ */

val fallbackChannels = listOf(

    SocialAppChannel(
        SocialType.WHATSAPP,
        "Harcourtians MCA Alumni Official",
        "Important alumni announcements and meet updates",
        "https://chat.whatsapp.com/FooywyRBD1CDtnhHY5U2lh"
    ),

    SocialAppChannel(
        SocialType.WHATSAPP,
        "NCR Harcourtians' Family GT",
        "Only messages related to GT are allowed in this group",
        "https://chat.whatsapp.com/LJRioHnlitA2FVvSy4bNtl?mode=gi_t"
    )

)

/* ------------------------------------------------ */
/* ICON MAPPER */
/* ------------------------------------------------ */

fun getSocialIcon(type: SocialType): Int {
    return when (type) {
        SocialType.WHATSAPP -> R.drawable.ic_whatsup
        SocialType.FACEBOOK -> R.drawable.ic_facebook
        SocialType.INSTAGRAM -> R.drawable.ic_insta
        SocialType.TWITTER -> R.drawable.ic_twitter
        SocialType.YOUTUBE -> R.drawable.ic_youtube
    }
}

fun getSocialChannels(): List<SocialAppChannel> {

    val remoteConfig = FirebaseRemoteConfig.getInstance()

    val json = remoteConfig.getString("social_channels")
    Log.e("Json", "Fetched social channels JSON: $json")
    if (json.isBlank()) return fallbackChannels

    return try {

        val jsonArray = JSONArray(json)

        val list = mutableListOf<SocialAppChannel>()

        for (i in 0 until jsonArray.length()) {

            val obj = jsonArray.getJSONObject(i)

            val type = SocialType.valueOf(obj.getString("type"))

            list.add(
                SocialAppChannel(
                    type = type,
                    title = obj.getString("title"),
                    description = obj.getString("description"),
                    link = obj.getString("link")
                )
            )
        }

        if (list.isEmpty()) fallbackChannels else list

    } catch (e: Exception) {
        Log.e("SocialChannels", "Error parsing social channels JSON", e)
        fallbackChannels
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppChannelsScreen(navController: NavController) {

    val context = LocalContext.current
    TrackScreen("whatsapp_channels_screen")

    val channels = getSocialChannels()

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Social Community")
                },

                navigationIcon = {

                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = {
                            navController.navigate(Routes.ADD_SOCIAL_CHANNEL)                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Add Channel"
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }

    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {

            item {

                Text(
                    text = "Choose a channel to stay connected",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            items(channels) { channel ->

                SocialChannelCard(channel, context)
            }
        }
    }
}
//@Composable
//fun WhatsAppChannelCard(channel: SocialAppChannel, context: Context) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp)
//            .clickable {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channel.link))
//                context.startActivity(intent)
//            },
//        shape = RoundedCornerShape(20.dp),
//        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_whatsup),
//                contentDescription = "title",
//                modifier = Modifier.size(60.dp),   // 🔥 120dp size
//                        tint = Color.Unspecified   // keep original WhatsApp green
//            )
//
//
//            Spacer(Modifier.width(12.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(channel.title,
//                    style = MaterialTheme.typography.titleMedium,
//                    fontWeight = FontWeight.Bold,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis)
//                Text(channel.description,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color(0xFF7A8194))
//            }
//
//            Row(
//                modifier = Modifier
//                    .background(
//                        color = Color(0xFF1E88E5), // Blue background
//                        shape = RoundedCornerShape(20.dp)
//                    )
//                    .padding(horizontal = 14.dp, vertical = 8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.GroupAdd,
//                    contentDescription = "Join Network",
//                    tint = Color.White,
//                    modifier = Modifier.size(18.dp)
//                )
//
//                Spacer(modifier = Modifier.width(6.dp))
//
//                Text(
//                    text = "Join",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color.White
//                )
//            }
//
//
//                   }
//    }
//}
/* ------------------------------------------------ */
/* CARD */
/* ------------------------------------------------ */

@Composable
fun SocialChannelCard(channel: SocialAppChannel, context: Context) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(channel.link)
                )

                context.startActivity(intent)
            },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = getSocialIcon(channel.type)),
                contentDescription = channel.title,
                modifier = Modifier.size(60.dp),
                tint = Color.Unspecified
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = channel.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = channel.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7A8194)
                )
            }

            JoinButton(channel.type)
        }
    }
}

/* ------------------------------------------------ */
/* JOIN BUTTON */
/* ------------------------------------------------ */

@Composable
fun JoinButton(type: SocialType) {

    val text = when (type) {
        SocialType.WHATSAPP -> "Join"
        SocialType.FACEBOOK -> "Like"
        SocialType.INSTAGRAM -> "Follow"
        SocialType.TWITTER -> "Follow"
        SocialType.YOUTUBE -> "Subscribe"
    }

    Row(
        modifier = Modifier
            .background(
                color = Color(0xFF1E88E5),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = androidx.compose.material.icons.Icons.Outlined.GroupAdd,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}