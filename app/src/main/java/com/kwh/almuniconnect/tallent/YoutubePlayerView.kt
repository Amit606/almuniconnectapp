package com.kwh.almuniconnect.tallent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YoutubePlayerView(videoUrl: String) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = {
            val youtubePlayerView = YouTubePlayerView(context)
            lifecycleOwner.lifecycle.addObserver(youtubePlayerView)

            youtubePlayerView.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        val videoId = extractYoutubeId(videoUrl)
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                }
            )
            youtubePlayerView
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
fun extractYoutubeId(url: String): String {
    return when {
        url.contains("youtu.be") -> {
            url.substringAfterLast("/")
        }
        url.contains("watch?v=") -> {
            url.substringAfter("watch?v=").substringBefore("&")
        }
        url.contains("shorts/") -> {
            url.substringAfter("shorts/").substringBefore("?")
        }
        else -> ""
    }
}