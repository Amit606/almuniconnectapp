import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.kwh.almuniconnect.network.AlumniDto
import com.kwh.almuniconnect.network.AlumniPagingSource
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

    private var ascendingOrder = false

    val alumniPagingFlow = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            AlumniPagingSource(
                repository = repository,
                ascending = ascendingOrder
            )
        }
    ).flow.cachedIn(viewModelScope)

    fun toggleSort() {
        ascendingOrder = !ascendingOrder
    }
}
//    fun applyFilter(branch: String?, year: String?) {
//        val yearInt = year?.toIntOrNull()
//
//        val filtered = originalList.filter {
//            (branch == null || it.courseName == branch) &&
//                    (yearInt == null || it.batch == yearInt)
//        }
//
//        _state.value = AlumniState.Success(
//            alumni = filtered,
//            totalCount = filtered.size,
//            pageNumber = pageNumber
//        )
//    }
//
//    fun clearFilter() {
//        _state.value = AlumniState.Success(
//            alumni = originalList,
//            totalCount = originalList.size,
//            pageNumber = pageNumber
//        )
//    }
//}


