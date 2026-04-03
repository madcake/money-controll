package shiny.mc.feature.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.KoinViewModel
import shiny.mc.services.store.dao.ExpenseDao

@KoinViewModel
class ExpensesListViewModel(
    private val expenseDao: ExpenseDao
) : ViewModel() {

    val items = expenseDao.getExpanses()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}