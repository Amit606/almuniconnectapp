package com.kwh.almuniconnect.tallent

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun YoutubePlayerWebView(videoUrl: String) {

    val videoId = extractVideoId(videoUrl)

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                loadData(
                    """
                    <html>
                        <body style="margin:0">
                            <iframe 
                                width="100%" 
                                height="100%" 
                                src="https://www.youtube.com/embed/$videoId"
                                frameborder="0"
                                allowfullscreen>
                            </iframe>
                        </body>
                    </html>
                    """.trimIndent(),
                    "text/html",
                    "utf-8"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}
fun extractVideoId(url: String): String {
    val regex = Regex("""(?:v=|\/)([0-9A-Za-z_-]{11}).*""")
    return regex.find(url)?.groupValues?.get(1) ?: url
}