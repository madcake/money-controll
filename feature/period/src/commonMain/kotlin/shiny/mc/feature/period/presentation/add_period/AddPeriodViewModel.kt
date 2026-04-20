package shiny.mc.feature.period.presentation.add_period

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.ports.app_config.SetCurrentPeriod
import shiny.mc.core.ports.period.CopyPeriod
import shiny.mc.core_ui.model.Command
import shiny.mc.core_ui.model.CommandState
import shiny.mc.core_ui.model.processCommand
import shiny.mc.feature.period.model.CopyPeriodValue
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AddPeriodViewModel(
    private val copyPeriod: CopyPeriod,
    private val setCurrentPeriod: SetCurrentPeriod
) : ViewModel() {

    private val copyPeriodCommand = MutableSharedFlow<Command<CopyPeriodValue>>()
    val copyPeriodState = copyPeriodCommand.processCommand { copyPeriod.copy(it.from, it.to) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), CommandState.Idle())

    private val newEmptyPeriodCommand = MutableSharedFlow<Command<PeriodDate>>()
    val newEmptyPeriodState = newEmptyPeriodCommand.processCommand(setCurrentPeriod::period)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), CommandState.Idle())

    fun copyPeriod(from: PeriodDate, to: PeriodDate) = viewModelScope.launch {
        copyPeriodCommand.emit(Command.Action(CopyPeriodValue(from, to)))
    }

    fun newEmptyPeriod(period: PeriodDate) = viewModelScope.launch {
        newEmptyPeriodCommand.emit(Command.Action(period))
    }

    fun reset() = viewModelScope.launch {
        copyPeriodCommand.emit(Command.Reset())
        newEmptyPeriodCommand.emit(Command.Reset())
    }
}