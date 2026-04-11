package shiny.mc.feature.period

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.coordinators.app_config.GetCurrentPeriod
import shiny.mc.core.coordinators.record.GetPeriodRecords
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.feature.period.model.ValueState
import shiny.mc.platform.format

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class PeriodViewModel(
    getCurrentPeriod: GetCurrentPeriod,
    private val getRecords: GetPeriodRecords,
) : ViewModel() {

    val period = getCurrentPeriod.period()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PeriodDate.default())
    val title = period.map {
        val (month, year) = it
        val date = LocalDate(year, month, 1)
        val format = LocalDate.Format {
            monthName(MonthNames.ENGLISH_FULL)
            char(' ')
            year()
        }
        date.format(format)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val records: StateFlow<List<RecordItem>> = period.flatMapLatest {
        val (month, year) = it
        getRecords.getRecords(month, year)
    }
    .mapLatest { items -> items.map { RecordItemImpl(it) }}
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

private class RecordItemImpl(data: Record) : RecordItem {
    override val id: String = data.id
    override val title: String = data.category.title
    override val scheduledValue: String = data.scheduledValue.format()
    override val realValue: String = data.realValue.format()
    override val valueState: ValueState = if (data.scheduledValue >= data.realValue) {
        ValueState.Surplus
    } else {
        ValueState.Deficit
    }
}