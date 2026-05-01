package shiny.mc.feature.transaction.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.koin.core.annotation.Named
import shiny.mc.core.domain.value.TransactionValue
import shiny.mc.core.ports.record.GetRecord
import shiny.mc.core.ports.transaction.AddRecordTransaction
import shiny.mc.core.ports.transaction.GetTransactionSuggestions
import shiny.mc.core.ports.ui.UIModel
import shiny.mc.core_ui.model.Command
import shiny.mc.core_ui.model.CommandState
import shiny.mc.core_ui.model.onSuccess
import shiny.mc.core_ui.model.processCommand

@OptIn(ExperimentalCoroutinesApi::class)
@Named("add_transaction")
class AddTransactionUIModel(
    private val getTransactionSuggestions: GetTransactionSuggestions,
    private val addRecordTransaction: AddRecordTransaction,
    private val getRecord: GetRecord,
    override val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()),
) : UIModel<AddTransactionUIState, AddTransactionCommand> {

    val recordId = MutableStateFlow("")
    val purpose = MutableStateFlow("")
    val value = MutableStateFlow("")
    val date = MutableStateFlow(0L)
    val command = MutableSharedFlow<Command<TransactionValue>>()

    val commandState: StateFlow<CommandState<Command<TransactionValue>>> = command.processCommand {
            addRecordTransaction.addTransaction(
                recordId = it.recordId,
                value = it.value,
                purpose = it.purpose,
                datetime = it.date,
            )
        }
        .onSuccess {
            purpose.update { "" }
            value.update { "" }
        }
        .stateIn(scope, SharingStarted.Eagerly, CommandState.Idle())

    val record = recordId
        .flatMapLatest { getRecord.getRecord(it) }
        .onEach { record ->
            record ?: return@onEach

            val start = LocalDateTime(
                month = record.period.month, year = record.period.year, day = 1,
                hour = 0, minute = 0, second = 1
            ).toInstant(TimeZone.UTC).toEpochMilliseconds()

            val end = LocalDateTime(
                month = (record.period.month + 1).takeIf { it <= 12 } ?: 1, year = (record.period.month + 1).takeIf { it <= 12 }
                    ?.let { record.period.year } ?: (record.period.year + 1), day = 1,
                hour = 0, minute = 0, second = 1
            ).toInstant(TimeZone.UTC).toEpochMilliseconds()
            val range = LongRange(start, end)

            if (!range.contains(date.value)) {
                date.update { start }
            }
        }

    val purposeSuggestions
        = combine(recordId, purpose, ::Pair)
        .flatMapLatest {
            val (recordId, purpose) = it
            getTransactionSuggestions.purposeSuggestions(recordId, purpose)
        }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val uiState: StateFlow<AddTransactionUIState> = shiny.mc.core.ports.ui.combine(
        record,
        purpose,
        purposeSuggestions,
        value,
        date,
        commandState,
        transform = ::AddTransactionUIState,
    ).stateIn(scope, SharingStarted.WhileSubscribed(5_000), AddTransactionUIState.Default)

    override fun sendCommand(command: AddTransactionCommand) {
        when (command) {
            is AddTransactionCommand.ChangeRecord -> recordId.update { command.recordId }
            is AddTransactionCommand.ChangeDate -> date.update { command.value }
            is AddTransactionCommand.ChangePurpose -> purpose.update { command.value }
            is AddTransactionCommand.ChangeValue -> value.update { command.value }
            is AddTransactionCommand.Save -> scope.launch {
                this@AddTransactionUIModel.command.emit(
                    Command.Action(
                        TransactionValue(
                            recordId = command.recordId,
                            purpose = command.purpose,
                            value = command.value,
                            date = command.date,
                        )
                    )
                )
            }
        }
    }

    override fun reset() {
        recordId.update { "" }
        purpose.update { "" }
        value.update { "" }
        date.update { 0L }
    }
}