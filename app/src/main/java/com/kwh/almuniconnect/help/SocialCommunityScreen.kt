package com.kwh.almuniconnect.help
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.ui.res.painterResource
import com.kwh.almuniconnect.R
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kwh.almuniconnect.Routes
import com.kwh.almuniconnect.analytics.TrackScreen
import org.json.JSONArray
import androidx.core.net.toUri


/* ------------------------------------------------ */
/* SOCIAL TYPE */
/* ------------------------------------------------ */

enum class SocialType {
    WHATSAPP, FACEBOOK, INSTAGRAM, TWITTER, YOUTUBE
}

/* ------------------------------------------------ */
/* MODEL */
/* ------------------------------------------------ */

data class SocialAppChannel(
    val type: SocialType,
    val title: String,
    val description: String,
    val link: String
)

/* ------------------------------------------------ */
/* FALLBACK */
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
        "Only messages related to GT are allowed",
        "https://chat.whatsapp.com/LJRioHnlitA2FVvSy4bNtl"
    )
)

/* ------------------------------------------------ */
/* ICON */
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

/* ------------------------------------------------ */
/* FETCH CHANNELS */
/* ------------------------------------------------ */

fun getSocialChannels(): List<SocialAppChannel> {

    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val json = remoteConfig.getString("social_channels")

    if (json.isBlank()) return fallbackChannels

    return try {
        val jsonArray = JSONArray(json)
        val list = mutableListOf<SocialAppChannel>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            list.add(
                SocialAppChannel(
                    type = SocialType.valueOf(obj.getString("type")),
                    title = obj.getString("title"),
                    description = obj.getString("description"),
                    link = obj.getString("link")
                )
            )
        }

        if (list.isEmpty()) fallbackChannels else list

    } catch (e: Exception) {
        fallbackChannels
    }
}

/* ------------------------------------------------ */
/* MAIN SCREEN */
/* ------------------------------------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialCommunityScreen(navController: NavController) {

    val context = LocalContext.current
    TrackScreen("social_community_screen")

    // ✅ FIXED (no recomposition issue)
    val channels by remember {
        mutableStateOf(getSocialChannels())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Social Community") },

                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                actions = {
                    IconButton(onClick = {
                        navController.navigate(Routes.ADD_SOCIAL_CHANNEL)
                    }) {
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

    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
        ) {

            /* ---------------- HEADER ---------------- */

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEFF6FF)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            "Stay Connected 💬",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Text(
                            "Join alumni channels & grow your network",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            /* ---------------- EMPTY ---------------- */

            if (channels.isEmpty()) {
                item {
                    Text(
                        "No channels available 🚀",
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }

            /* ---------------- LIST ---------------- */

            items(channels) { channel ->
                SocialChannelCard(channel, context)
            }
        }
    }
}

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
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, channel.link.toUri())
                )
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE6E9F0))
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            /* ---------- ICON ---------- */

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Color(0xFFF1F5F9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(getSocialIcon(channel.type)),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Unspecified
                )
            }

            Spacer(Modifier.width(14.dp))

            /* ---------- TEXT ---------- */

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = channel.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = channel.description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                /* ---------- PLATFORM TAG ---------- */

                Text(
                    text = channel.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = 10.sp,
                    color = Color(0xFF1E3A8A),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.width(10.dp))

            /* ---------- JOIN BUTTON ---------- */

            JoinButton(channel.type)
        }
    }
}
/* ------------------------------------------------ */
/* BUTTON */
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
            .background(Color(0xFF1E88E5), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Outlined.GroupAdd,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

/* ------------------------------------------------ */
/* JOIN BUTTON */
/* ------------------------------------------------ */
