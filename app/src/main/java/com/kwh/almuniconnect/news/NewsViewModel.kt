package com.kwh.almuniconnect.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NewsState {
    object Loading : NewsState()
    data class Success(
        val news: List<NewsItem>,
        val totalCount: Int
    ) : NewsState()
    data class Error(val message: String) : NewsState()
}

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<NewsState>(NewsState.Loading)
    val state: StateFlow<NewsState> = _state

    private var pageNumber = 1
    private val pageSize = 10
    private val newsList = mutableListOf<NewsItem>()

    fun loadNews(reset: Boolean = false) {
        viewModelScope.launch {

            if (reset) {
                pageNumber = 1
                newsList.clear()
            }

            _state.value = NewsState.Loading

            repository.getNews(pageNumber, pageSize)
                .onSuccess { response ->

                    // ✅ SAFE: handle null items
                    val safeItems = response.items ?: emptyList()

                    newsList.addAll(safeItems)

                    _state.value = NewsState.Success(
                        news = newsList.toList(),   // immutable copy
                        totalCount = response.totalCount
                    )

                    pageNumber++
                }
                .onFailure {
                    _state.value = NewsState.Error(
                        it.message ?: "Failed to load news"
                    )
                }
        }
    }
}

