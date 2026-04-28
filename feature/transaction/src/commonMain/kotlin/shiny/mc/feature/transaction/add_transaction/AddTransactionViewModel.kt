package shiny.mc.feature.transaction.add_transaction

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.domain.value.TransactionValue
import shiny.mc.core.ports.record.GetRecord
import shiny.mc.core.ports.transaction.AddRecordTransaction
import shiny.mc.core.ports.transaction.GetTransactionSuggestions
import shiny.mc.core_ui.model.Command
import shiny.mc.core_ui.model.CommandState
import shiny.mc.core_ui.model.action
import shiny.mc.core_ui.model.onSuccess
import shiny.mc.core_ui.model.processCommand
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), "")

    val record = recordId
        .filterNotNull()
        .flatMapLatest { getRecord.getRecord(it) }
        .filterNotNull()
        .onEach { record ->
            if (date.value == 0L) {
                val start = LocalDateTime(
                    month = record.period.month, year = record.period.year, day = 1,
                    hour = 0, minute = 0, second = 1
                ).toInstant(TimeZone.UTC).toEpochMilliseconds()

                val end = LocalDateTime(
                    month = (record.period.month + 1).takeIf { it <= 12 } ?: 1, year = (record.period.month + 1).takeIf { it <= 12 }
                        ?.let { record.period.year } ?: (record.period.year + 1), day = 1,
                    hour = 0, minute = 0, second = 1
                ).toInstant(TimeZone.UTC).toEpochMilliseconds()

                if (date.value !in LongRange(start, end)) {
                    date.update { start }
                }
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

    private val command = MutableSharedFlow<Command<TransactionValue>>()
    val commandState = command.processCommand {
            addRecordTransaction.addTransaction(
                recordId = it.recordId,
                value = it.value,
                purpose = it.purpose,
                datetime = it.date,
            )
        }
        .onSuccess { resetInputs() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, CommandState.Idle())


    fun add(recordId: String) = viewModelScope.launch {
        command.action(
            TransactionValue(
                recordId = recordId,
                purpose = purpose.value,
                value = valueState.text.toString(),
                date = date.value,
            )
        )
    }

    fun date(date: Long?) {
        this.date.update {
            date ?: Clock.System.now().toEpochMilliseconds()
        }
    }

    private fun resetCommand() = viewModelScope.launch {
        command.emit(Command.Reset())
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

