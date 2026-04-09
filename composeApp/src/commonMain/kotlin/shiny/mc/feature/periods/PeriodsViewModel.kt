package shiny.mc.feature.periods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import shiny.mc.core.coordinators.app_config.GetCurrentPeriod
import shiny.mc.core.coordinators.app_config.SetCurrentPeriod
import shiny.mc.core.coordinators.period.GetPeriods
import shiny.mc.core.domain.value.Period
import kotlin.time.Duration.Companion.seconds

class PeriodsViewModel(
    private val getCurrentPeriod: GetCurrentPeriod,
    private val setCurrentPeriod: SetCurrentPeriod,
    private val getPeriods: GetPeriods,
) : ViewModel() {

    val periods = getPeriods.periods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), emptyList())

    fun newPeriod() { }

    fun setCurrent(item: Period) = viewModelScope.launch {
        setCurrentPeriod.period(item.date)
    }
}