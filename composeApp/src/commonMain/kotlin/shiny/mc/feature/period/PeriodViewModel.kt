package shiny.mc.feature.period

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.coordinators.record.GetPeriodRecords
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.feature.period.model.ValueState
import shiny.mc.platform.format
import kotlin.time.Clock

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class PeriodViewModel(
    private val getRecords: GetPeriodRecords
) : ViewModel() {

    private val _period = MutableStateFlow(getCurrentPeriod())
    val period = _period.map {
        val (month, year) = it
        val date = LocalDate(year, month, 1)
        val format = LocalDate.Format {
            monthName(MonthNames.ENGLISH_FULL)
            year()
        }
        date.format(format)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val records: StateFlow<List<RecordItem>> = _period.flatMapLatest {
        val (month, year) = it
        getRecords.getRecords(month, year)
    }
    .mapLatest { items -> items.map { RecordItemImpl(it) }}
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setPeriod(month: Int, year: Int) {
        _period.update { Pair(month, year) }
    }

    private fun getCurrentPeriod(): Pair<Int, Int> {
        val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return Pair(dateTime.month.number, dateTime.year)
    }
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