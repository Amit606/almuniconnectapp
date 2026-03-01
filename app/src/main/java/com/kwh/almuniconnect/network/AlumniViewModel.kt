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
import com.kwh.almuniconnect.network.YearUiModel
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

    /* ---------------- PAGING SECTION ---------------- */

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


    /* ---------------- FULL LIST SECTION ---------------- */

    var allAlumni by mutableStateOf<List<AlumniDto>>(emptyList())
        private set

    fun setAlumniList(list: List<AlumniDto>) {
        allAlumni = list
    }
    fun loadAllAlumni() {
        viewModelScope.launch {

            val result = repository.getAlumniList(
                pageNumber = 1,
                pageSize = 100,
                ascending = false
            )

            result.onSuccess { response ->
                allAlumni = response.items ?: emptyList()
                Log.e("VM_DEBUG", "Loaded size: ${allAlumni.size}")
            }

            result.onFailure {
                Log.e("VM_ERROR", it.message ?: "Unknown error")
            }
        }
    }

    /* ---------------- YEAR GROUPING ---------------- */

    fun getYearsByBranch(branchShort: String): List<YearUiModel> {

        return allAlumni
            .filter { it.courseName.equals(branchShort, ignoreCase = true) }
            .groupBy { it.batch }
            .map { (year, list) ->
                YearUiModel(
                    year = year.toString(),
                    alumniCount = list.size
                )
            }
            .sortedByDescending { it.year.toInt() }
    }

    /* ---------------- FILTER BRANCH + YEAR ---------------- */

    fun getAlumniByBranchAndYear(
        branchShort: String,
        year: Int
    ): List<AlumniDto> {

        return allAlumni.filter {
            it.courseName.equals(branchShort, true) &&
                    it.batch == year
        }
    }
}
