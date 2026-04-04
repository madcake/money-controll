package shiny.mc.feature.categories

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.coordinators.category.DeleteCategory
import shiny.mc.core.coordinators.category.SearchCategories
import shiny.mc.core.coordinators.record.AddRecords
import shiny.mc.core.domain.entity.Category

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class CategoriesViewModel(
    private val searchCategories: SearchCategories,
    private val deleteCategory: DeleteCategory,
    private val addRecords: AddRecords
) : ViewModel() {
    val queryState = TextFieldState("")
    val query = snapshotFlow { queryState.text.toString() }

    val categories = query.flatMapLatest { searchCategories.searchCategories(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), emptyList())

    fun remoteCategory(id: Long?) {
        id ?: return
        viewModelScope.launch { deleteCategory.deleteCategory(id) }
    }

       fun addToPeriod(categories: List<Category>, month: Int, year: Int) = viewModelScope.launch {
           addRecords.addRecords(categories, month, year)
       }
}