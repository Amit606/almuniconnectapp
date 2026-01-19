import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kwh.almuniconnect.network.AlumniDto
import com.kwh.almuniconnect.network.AlumniRepository
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ---------- UI STATE ----------
sealed class AlumniState {
    object Loading : AlumniState()
    data class Success(
        val alumni: List<AlumniDto>,
        val totalCount: Int,
        val pageNumber: Int
    ) : AlumniState()

    data class Error(val message: String) : AlumniState()
}

// ---------- VIEWMODEL ----------
class AlumniViewModel(
    private val repository: AlumniRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AlumniState>(AlumniState.Loading)
    val state: StateFlow<AlumniState> = _state

    private var pageNumber = 1
    private val pageSize = 10
    private val alumniList = mutableListOf<AlumniDto>()
    private var isLastPage = false
    private var isLoading = false

    fun loadAlumni(reset: Boolean = false) {
        if (isLoading || isLastPage) return

        viewModelScope.launch {
            isLoading = true

            if (reset) {
                pageNumber = 1
                isLastPage = false
                alumniList.clear()
                _state.value = AlumniState.Loading
            }

            repository.getAlumniList(pageNumber, pageSize)
                .onSuccess { response ->

                    // ✅ SAFE: handle null list from backend
                    val safeItems = response.items ?: emptyList()

                    alumniList.addAll(safeItems)

                    isLastPage = alumniList.size >= response.totalCount

                    _state.value = AlumniState.Success(
                        alumni = alumniList.toList(), // immutable copy
                        totalCount = response.totalCount,
                        pageNumber = pageNumber
                    )

                    pageNumber++
                }
                .onFailure { error ->
                    _state.value = AlumniState.Error(
                        error.message ?: "Failed to load alumni"
                    )
                }

            isLoading = false
        }
    }
}

