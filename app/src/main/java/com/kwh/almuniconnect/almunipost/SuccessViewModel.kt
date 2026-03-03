package com.kwh.almuniconnect.almunipost

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuccessViewModel(
    private val context: Application
) : AndroidViewModel(context) {

    private val _alumniList = MutableStateFlow<List<AlumniStory>>(emptyList())
    val alumniList: StateFlow<List<AlumniStory>> = _alumniList

    init {
        loadAlumni()
    }

    private fun loadAlumni() {
        viewModelScope.launch {
            _alumniList.value =
                SuccessRepository.fetchAlumni(context)
        }
    }
}