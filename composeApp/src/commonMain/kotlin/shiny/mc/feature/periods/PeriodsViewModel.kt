package shiny.mc.feature.periods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import shiny.mc.core.coordinators.app_config.GetCurrentPeriodDate
import shiny.mc.core.coordinators.app_config.SetCurrentPeriod
import shiny.mc.core.coordinators.period.GetPeriods
import shiny.mc.core.domain.value.Period
import shiny.mc.core.domain.value.PeriodDate
import kotlin.time.Duration.Companion.seconds

class PeriodsViewModel(
    private val getCurrentPeriodDate: GetCurrentPeriodDate,
    private val setCurrentPeriod: SetCurrentPeriod,
    private val getPeriods: GetPeriods,
) : ViewModel() {

    val periods = getPeriods.periods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), emptyList())

    fun newPeriod(month: Int, year: Int) = viewModelScope.launch {
        setCurrentPeriod.period(PeriodDate(month, year))
    }

    fun setCurrent(item: Period) = viewModelScope.launch {
        setCurrentPeriod.period(item.date)
    }
}