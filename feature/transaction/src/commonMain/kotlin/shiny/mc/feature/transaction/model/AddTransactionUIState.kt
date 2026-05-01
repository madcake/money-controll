package shiny.mc.feature.transaction.model

import shiny.mc.core.domain.entity.RecordInfo
import shiny.mc.core.domain.value.TransactionValue
import shiny.mc.core_ui.model.Command
import shiny.mc.core_ui.model.CommandState

data class AddTransactionUIState(
    val record: RecordInfo?,
    val purpose: String,
    val purposeSuggestions: List<String>,
    val value: String,
    val date: Long,
    val saveState: CommandState<Command<TransactionValue>>,
) {
    companion object {
        val Default = AddTransactionUIState(
            record = null,
            purpose = "",
            purposeSuggestions = emptyList(),
            value = "",
            date = 0L,
            saveState = CommandState.Idle(),
        )
    }
}