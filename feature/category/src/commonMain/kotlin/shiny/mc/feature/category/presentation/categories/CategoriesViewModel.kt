package shiny.mc.feature.category.presentation.categories

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.dto.Category
import shiny.mc.core.ports.category.DeleteCategory
import shiny.mc.core.ports.category.SearchCategories
import shiny.mc.core.ports.record.ChangeRecords
import shiny.mc.core.ports.record.GetPeriodRecords

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class CategoriesViewModel(
    private val searchCategories: SearchCategories,
    private val getPeriodRecords: GetPeriodRecords,
    private val deleteCategory: DeleteCategory,
    private val changeRecords: ChangeRecords,
) : ViewModel() {
    val queryState = TextFieldState("")
    val query = snapshotFlow { queryState.text.toString() }

    val categories = query.flatMapLatest { searchCategories.searchCategories(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), emptyList())

    fun selected(month: Int, year: Int) = getPeriodRecords.getRecords(month, year)
        .mapLatest { items -> items.map { it.category.id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeCategory(id: Long?) {
        id ?: return
        viewModelScope.launch { deleteCategory.deleteCategory(id) }
    }

    fun addToPeriod(category: Category, month: Int, year: Int) = viewModelScope.launch {
        changeRecords.changeRecord(category, month, year)
    }
}