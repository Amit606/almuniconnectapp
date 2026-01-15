package com.kwh.almuniconnect.intro

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwh.almuniconnect.R
import kotlinx.coroutines.launch

data class IntroPage(
    val title: String,
    val description: String,
    val image: Int
)
val introPages = listOf(
    IntroPage(
        "Connect with Alumni",
        "Stay connected with batchmates, seniors, and the global HBTU alumni network.",
        R.drawable.first_screen
    ),
    IntroPage(
        "Explore Opportunities",
        "Access jobs, referrals, events, and mentorship opportunities from alumni.",
        R.drawable.second_screen
    ),
    IntroPage(
        "Give Back to HBTU",
        "Contribute to students, campus initiatives, and the future of HBTU.",
        R.drawable.third_screen
    )

)



/**
 * IntroScreen now accepts a callback that's invoked when user finishes/skips the intro.
 * This keeps navigation inside the NavGraph.
 */
@Composable
fun IntroScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState { introPages.size }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF3EE)) // soft background
    ) { page ->
        FullScreenIntroPageWithBg(
            page = introPages[page],
            pageIndex = page,
            pageCount = introPages.size,
            isLast = page == introPages.lastIndex,
            onFinish = onFinish
        )
    }
}
@Composable
fun FullScreenIntroPageWithBg(
    page: IntroPage,
    pageIndex: Int,
    pageCount: Int,
    isLast: Boolean,
    onFinish: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔹 Background Image (FrameLayout background)
        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🔹 Soft overlay (IMPORTANT for readability)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAAFFFFFF))
        )

        // 🔹 Foreground Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // 📝 Title & Description
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    page.title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    page.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // 🔽 Bottom Controls
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                PagerIndicator(
                    size = pageCount,
                    current = pageIndex
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isLast) onFinish()
                    },
                    enabled = isLast,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text(
                        if (isLast) "Get Started" else "Swipe →"
                    )
                }
            }
        }
    }
}







@Composable
fun PagerIndicator(
    size: Int,
    current: Int
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(size) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (index == current) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == current)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
            )
        }
    }
}

