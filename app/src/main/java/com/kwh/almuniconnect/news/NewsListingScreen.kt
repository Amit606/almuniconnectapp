package com.kwh.almuniconnect.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kwh.almuniconnect.api.ApiService
import com.kwh.almuniconnect.api.NetworkClient
import com.kwh.almuniconnect.appbar.HBTUTopBar
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import com.kwh.almuniconnect.R
import com.kwh.almuniconnect.analytics.TrackScreen
import com.kwh.almuniconnect.utils.CommonEmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListingScreen(navController: NavController) {

    val apiService = remember {
        NetworkClient.createService(ApiService::class.java)
    }
    val repository = remember { NewsRepository(apiService) }
    val viewModel: NewsViewModel = viewModel(
        factory = NewsViewModelFactory(repository)
    )

    val state by viewModel.state.collectAsState()
    TrackScreen("news_listing_screen")

    LaunchedEffect(Unit) {
        viewModel.loadNews()
    }

    Scaffold(
        topBar = {
            HBTUTopBar(
                title = "News",
                navController = navController
            )
        }
    ) { paddingValues ->

        when (state) {

            is NewsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is NewsState.Error -> {
                CommonEmptyState(
                    title = "No Upcoming Events",
                    message = "There are no upcoming events right now.\nPlease check back later.",
                    lottieRes = R.raw.no_events,
                    actionText = "Refresh",
                    onActionClick = { viewModel.loadNews() }
                )
            }

            is NewsState.Success -> {
                val newsList = (state as NewsState.Success).news

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = paddingValues
                ) {
                    items(newsList) { news ->
                        NewsCard(news)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCard(news: NewsItem) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {

            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = news.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Published: ${news.publishedAt}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
