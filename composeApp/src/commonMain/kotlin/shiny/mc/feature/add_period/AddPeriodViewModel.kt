package shiny.mc.feature.add_period

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import shiny.mc.core.coordinators.app_config.SetCurrentPeriod
import shiny.mc.core.coordinators.period.CopyPeriod
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.core.model.CommandState
import shiny.mc.feature.add_period.model.AddPeriodCommand
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AddPeriodViewModel(
    private val copyPeriod: CopyPeriod,
    private val setCurrentPeriod: SetCurrentPeriod
) : ViewModel() {

    val command: StateFlow<AddPeriodCommand>
        field = MutableStateFlow<AddPeriodCommand>(AddPeriodCommand.None)

    val commandState = command
        .filter { it != AddPeriodCommand.None }
        .flatMapLatest { command ->
            flow {
                when (command) {
                    is AddPeriodCommand.Add -> {
                        emit(CommandState.Processing(command))
                        setCurrentPeriod.period(command.date)
                        emit(CommandState.Success(command))
                    }
                    is AddPeriodCommand.Copy -> try {
                        emit(CommandState.Processing(command))
                        copyPeriod.copy(command.from, command.to)
                        setCurrentPeriod.period(PeriodDate(command.to.month, command.to.year))
                        emit(CommandState.Success(command))
                    } catch (err: Throwable) {
                        emit(CommandState.Failure(err, command))
                    }
                    is AddPeriodCommand.CopySelectTo -> emit(CommandState.Processing(command))
                    AddPeriodCommand.None,
                    AddPeriodCommand.Reset -> emit(CommandState.Idle())
                }
            }
        }
        .onEach {
            when (it) {
                is CommandState.Success -> when (it.command) {
                    is AddPeriodCommand.Copy -> setCurrentPeriod.period(period = it.command.to)
                    else -> {}
                }
                else -> {}
            }
        }
        .onEach {
            when (it) {
                is CommandState.Processing<*> -> {}
                is CommandState.Failure<*>,
                is CommandState.Idle<*>,
                is CommandState.Success<*> -> command.update { AddPeriodCommand.None }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), CommandState.Idle())

    fun add(date: PeriodDate) {
        command.update { AddPeriodCommand.Add(date) }
    }

    fun copy(from: PeriodDate) {
        command.update { AddPeriodCommand.CopySelectTo(from) }
    }

    fun copy(from: PeriodDate, to: PeriodDate) {
        command.update { AddPeriodCommand.Copy(from, to) }
    }

    fun reset() {
        command.update { AddPeriodCommand.Reset }
    }
}