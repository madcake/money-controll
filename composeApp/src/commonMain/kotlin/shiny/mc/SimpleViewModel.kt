package shiny.mc

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
class SimpleViewModel constructor(
    private val expenseDao: ExpenseDao
) : ViewModel() {

    val items = expenseDao.getExpanses()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.insert(
                Expense(
                    title = "Test ${Clock.System.now().toEpochMilliseconds()}",
                    image = "https://sun9-79.userapi.com/s/v1/ig2/lEJS9v3uaWZsP31y7e_yElKF6JXqi7C1fDRuPaoE4BhKqULlCP-Z0F_1lvD2iK2LiiNvczZo63ZHI7KPnxQseDAq.jpg?quality=95&as=32x32,48x48,72x72,108x108,160x160,240x240,360x360,480x480,540x540,640x640,720x720,1080x1080,1280x1280,1440x1440,2048x2048&from=bu&cs=2048x0",
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