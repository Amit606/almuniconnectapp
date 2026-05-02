package com.kwh.almuniconnect.branding

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.kwh.almuniconnect.appbar.HBTUTopBar

@Composable
fun WebViewScreen(
    url: String,
    navController: NavController
) {

    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "Product/Service Details",
                navController = navController
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->

                    WebView(context).apply {

                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true

                        webChromeClient = object : WebChromeClient() {

                            override fun onProgressChanged(
                                view: WebView?,
                                newProgress: Int
                            ) {
                                progress = newProgress
                                isLoading = newProgress < 100
                            }
                        }

                        webViewClient = object : WebViewClient() {

                            override fun onPageFinished(
                                view: WebView?,
                                url: String?
                            ) {
                                isLoading = false
                            }
                        }

                        loadUrl(url)
                    }
                }
            )

            // ⭐ Top progress bar
            if (isLoading) {

                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )

                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}