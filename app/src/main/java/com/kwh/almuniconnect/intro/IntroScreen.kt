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
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val imageRes: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
)

val introPages = listOf(
    IntroPage(R.drawable.hbtu, R.string.welcome_msg, R.string.welcome_des),
    IntroPage(R.drawable.hbtu, R.string.whats_up_saver, R.string.whats_up_saver_des),
    IntroPage(R.drawable.hbtu, R.string.smart_cleaner_title, R.string.smart_cleaner_desc),
    IntroPage(R.drawable.hbtu, R.string.secure_fast_title, R.string.secure_fast_desc),
    IntroPage(R.drawable.hbtu, R.string.stay_organized_title, R.string.stay_organized_desc),
    IntroPage(R.drawable.hbtu, R.string.get_started_title, R.string.get_started_desc)
)

/**
 * IntroScreen now accepts a callback that's invoked when user finishes/skips the intro.
 * This keeps navigation inside the NavGraph.
 */
@Composable
fun IntroScreen(onContinue: () -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { introPages.size })

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(color = Color(0xFF142338), shape = RoundedCornerShape(0.dp))
            ) { page ->
                IntroPageContent(page = introPages[page])
            }

            BottomControls(pagerState = pagerState, onContinue = onContinue)
        }
    }
}

@Composable
fun IntroPageContent(page: IntroPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = page.title),
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = page.description),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun BottomControls(pagerState: PagerState, onContinue: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF142338)) // ✅ set your background color here

            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 16.dp, vertical = 20.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Skip Button
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (pagerState.currentPage < introPages.size - 1) {
                TextButton(onClick = onContinue) {
                    Text(text = stringResource(id = R.string.skip), color = Color.White,)
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }
        }

        // Center: Dots Indicator
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            PageIndicator(pageCount = introPages.size, currentPage = pagerState.currentPage)
        }

        // Right: Next or Get Started Button
        Box(modifier = Modifier.weight(1f),

            contentAlignment = Alignment.CenterEnd) {
            if (pagerState.currentPage < introPages.size - 1) {
                Button(onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }) {
                    Text(text = stringResource(id = R.string.next),color = Color.White,)
                }
            } else {
                Button(onClick = onContinue) {
                    Text(text = stringResource(id = R.string.get_started),color = Color.White,)
                }
            }
        }
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pageCount) { index ->
            val color = if (index == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 14.dp else 10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
