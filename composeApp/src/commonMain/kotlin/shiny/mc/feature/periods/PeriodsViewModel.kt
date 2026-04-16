package shiny.mc.feature.periods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import shiny.mc.core.dto.Period
import shiny.mc.core.ports.app_config.GetCurrentPeriodDate
import shiny.mc.core.ports.app_config.SetCurrentPeriod
import shiny.mc.core.ports.period.GetPeriods
import kotlin.time.Duration.Companion.seconds

class PeriodsViewModel(
    getCurrentPeriodDate: GetCurrentPeriodDate,
    getPeriods: GetPeriods,
    private val setCurrentPeriod: SetCurrentPeriod,
) : ViewModel() {

    val periods = getPeriods.periods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), emptyList())

    val current = getCurrentPeriodDate.period()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), null)

    fun setCurrent(item: Period) = viewModelScope.launch {
        setCurrentPeriod.period(item.date)
    }
}
