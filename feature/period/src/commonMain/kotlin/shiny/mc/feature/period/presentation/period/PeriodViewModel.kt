package shiny.mc.feature.period.presentation.period

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
import shiny.mc.core.domain.entity.RecordInfo
import shiny.mc.core.domain.value.default
import shiny.mc.core.domain.value.format
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.ports.app_config.GetCurrentPeriodDate
import shiny.mc.core.ports.period.GetPeriod
import shiny.mc.core.ports.record.GetPeriodRecords

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class PeriodViewModel(
    getCurrentPeriodDate: GetCurrentPeriodDate,
    private val getRecords: GetPeriodRecords,
    private val getPeriod: GetPeriod,
) : ViewModel() {

    val periodDate = getCurrentPeriodDate.period()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),PeriodDate.default())

    val title = periodDate.map { it.format() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private val period = periodDate.flatMapLatest { getPeriod.period(it) }

    val periodValues = period.mapLatest { it?.values }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val records = periodDate.flatMapLatest {
        getRecords.getRecords(it.month, it.year)
    }

    val outRecords: StateFlow<List<RecordInfo>> = records.map { items ->
        items.filter { it.categoryType == CategoryType.Out }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val inRecords: StateFlow<List<RecordInfo>> = records.map { items ->
        items.filter { it.categoryType == CategoryType.In }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}