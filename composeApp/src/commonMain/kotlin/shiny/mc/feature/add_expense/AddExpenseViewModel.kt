package shiny.mc.feature.add_expense

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import shiny.mc.services.store.dao.ExpenseDao
import shiny.mc.services.store.entity.Expense
import shiny.mc.services.store.entity.ExpenseType
import shiny.mc.services.store.entity.RepeatType
import kotlin.time.Clock

@KoinViewModel
class AddExpenseViewModel(
    private val expenseDao: ExpenseDao // TODO: Add coordinator
) : ViewModel() {
    val titleState: TextFieldState = TextFieldState("")
    val valueState: TextFieldState = TextFieldState("")
    private val title = snapshotFlow { titleState.text.toString() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    fun onSave() {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.insert(
                Expense(
                    title = title.value,
                    image = "",
                    description = "Some description",
                    repeat = RepeatType.MONTH,
                    plannedValue = 190.2,
                    realValue = 19.3,
                    type = ExpenseType.Schedule,
                    order = 1,
                    createAt = Clock.System.now().toEpochMilliseconds(),
                    updatedAt = Clock.System.now().toEpochMilliseconds(),
                )
            )
        }
    }
}