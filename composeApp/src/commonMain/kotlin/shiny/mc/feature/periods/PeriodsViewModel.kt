package shiny.mc.feature.periods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import shiny.mc.core.coordinators.period.GetPeriods
import kotlin.time.Duration.Companion.seconds

class PeriodsViewModel(
    private val getPeriods: GetPeriods,
) : ViewModel() {

    val periods = getPeriods.periods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), emptyList())
}