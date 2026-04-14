package shiny.mc.feature.add_transaction

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.transaction.AddRecordTransaction
import shiny.mc.core.coordinators.transaction.GetTransactionSuggestions
import shiny.mc.core.model.CommandState
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class AddTransactionViewModel(
//    @InjectedParam private val recordId: String,
    private val addRecordTransaction: AddRecordTransaction,
    private val getRecord: GetRecord,
    private val getTransactionSuggestions: GetTransactionSuggestions,
) : ViewModel() {

    val recordId = MutableStateFlow<String?>(null)

    val purposeState = TextFieldState()
    val valueState = TextFieldState()
    val date: StateFlow<Long>
        field = MutableStateFlow<Long>(0)
    private val purpose = snapshotFlow { purposeState.text.toString() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), "")
    private val value = snapshotFlow { valueState.text.toString() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), "")

    val record = recordId.filterNotNull().flatMapLatest { getRecord.getRecord(it) }
        .filterNotNull()
        .onEach { record ->
            if (date.value == 0L) {
                val millis = LocalDateTime(
                    month = record.month, year = record.year, day = 1,
                    hour = 0, minute = 0, second = 1
                ).toInstant(TimeZone.UTC).toEpochMilliseconds()
                date.update { millis }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val purposeSuggestions = combine(
        recordId.filterNotNull(),
        purpose
    ) { recordId, query -> Pair(recordId, query) }
        .onStart { delay(500) }
        .flatMapLatest { data ->
            val (recordId, query) = data
            getTransactionSuggestions.purposeSuggestions(recordId, query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), emptyList())

    val command: StateFlow<AddTransactionCommand>
        field = MutableStateFlow<AddTransactionCommand>(AddTransactionCommand.None)

    val commandState = combine(
        purpose.onEach { resetCommand() },
        value.onEach { resetCommand() },
        date.onEach { resetCommand() },
        command,
    ) { purpose, value, date, command ->
        when (command) {
            is AddTransactionCommand.Add -> AddTransactionCommand.Save(
                recordId = command.recordId,
                purpose =  purpose,
                value = value,
                date = date,
            )
            is AddTransactionCommand.Save -> command
            AddTransactionCommand.None -> null
        }
    }
    .filterNotNull()
    .flatMapLatest { command ->
        val (recordId, purpose, value, date) = command
        flow<CommandState<AddTransactionCommand>> {
            emit(CommandState.Processing(command))

            try {
                addRecordTransaction.addTransaction(
                    recordId = recordId,
                    value = value,
                    purpose = purpose,
                    datetime = date,
                )
                emit(CommandState.Success(command))
            } catch (err: Throwable) {
                emit(CommandState.Failure(err, command))
            }
        }
    }
    .onEach { state ->
        when (state) {
            is CommandState.Failure -> resetCommand()
            is CommandState.Idle -> resetCommand()
            is CommandState.Success -> {
                resetCommand()
                resetInputs()
            }
            is CommandState.Processing -> {}
        }
    }
    .stateIn(viewModelScope, SharingStarted.Eagerly, CommandState.Idle())

    fun add(recordId: String) {
        command.update { AddTransactionCommand.Add(recordId) }
    }

    fun date(date: Long?) {
        this.date.update {
            date ?: Clock.System.now().toEpochMilliseconds()
        }
    }

    private fun resetCommand() {
        command.update { AddTransactionCommand.None }
    }

    private fun resetInputs() {
        valueState.clearText()
        purposeState.clearText()
    }

    fun reset() {
        resetCommand()
        resetInputs()
    }
}

sealed interface AddTransactionCommand {
    object None : AddTransactionCommand
    data class Add(val recordId: String) : AddTransactionCommand
    data class Save(
        val recordId: String,
        val purpose: String,
        val value: String,
        val date: Long,
    ) : AddTransactionCommand
}