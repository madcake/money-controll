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
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.Record
import shiny.mc.core.dto.ValueState
import shiny.mc.core.ports.app_config.GetCurrentPeriodDate
import shiny.mc.core.ports.period.GetPeriod
import shiny.mc.core.ports.record.GetPeriodRecords
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.platform.format

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class PeriodViewModel(
    getCurrentPeriodDate: GetCurrentPeriodDate,
    private val getRecords: GetPeriodRecords,
    private val getPeriod: GetPeriod,
) : ViewModel() {

    val periodDate = getCurrentPeriodDate.period()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PeriodDate(4, 2026)/* TODO: PeriodDate.default()*/)

    val title = periodDate.map { it.toString() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private val period = periodDate.flatMapLatest { getPeriod.period(it) }

    val periodValues = period.mapLatest { it?.values }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val records = periodDate.flatMapLatest {
        CategoryType.Out
        getRecords.getRecords(it.month, it.year)
    }

    val outRecords: StateFlow<List<RecordItem>> = records.map { items ->
        items.filter { it.category.type == CategoryType.Out }
    }
    .mapLatest { items -> items.map { RecordItemImpl(it) }}
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inRecords: StateFlow<List<RecordItem>> = records.map { items ->
        items.filter { it.category.type == CategoryType.In }
    }
    .mapLatest { items -> items.map { RecordItemImpl(it) }}
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

private class RecordItemImpl(data: Record) : RecordItem {
    override val id: String = data.id
    override val title: String = data.category.title
    override val scheduledValue: String = data.estimateValue.format()
    override val realValue: String = data.realValue.format()
    override val valueState: ValueState = if (data.estimateValue >= data.realValue) {
        ValueState.Surplus
    } else {
        ValueState.Deficit
    }
}