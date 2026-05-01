package com.kwh.almuniconnect.branding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.model.ProductServiceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductServiceViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<ProductServiceItem>>(emptyList())
    val products: StateFlow<List<ProductServiceItem>> = _products

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun loadProducts() {

        _loading.value = true

        RemoteConfigManager.fetchProducts { list ->

            viewModelScope.launch {

                _products.value = list
                _loading.value = false

            }
        }
    }
}