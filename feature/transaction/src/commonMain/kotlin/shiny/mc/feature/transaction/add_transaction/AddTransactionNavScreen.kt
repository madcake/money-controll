package shiny.mc.feature.transaction.add_transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.feature.transaction.model.AddTransactionCommand
import shiny.mc.feature.transaction.model.AddTransactionUIState
import shiny.mc.feature.transaction.model.AddTransactionViewModel

@Composable
fun AddTransactionNavScreen(
    onCancel: () -> Unit,
    recordId: String,
    viewModel: AddTransactionViewModel = koinViewModel(),//(key = recordId) { parametersOf(recordId) },
) {
    DisposableEffect(recordId) {

        viewModel.sendCommand(AddTransactionCommand.ChangeRecord(recordId))

        onDispose {
            viewModel.reset()
        }
    }

    val state: AddTransactionUIState by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.record == null) {
        return
    }
    AddTransactionScene(
        recordMonth = state.record?.period?.month ?: 0,
        recordYear = state.record?.period?.year ?: 0,
        state = state.saveState,
        value = state.value,
        purpose = state.purpose,
        purposeSuggestions = state.purposeSuggestions,
        date = state.date,
        onPurposeChange = { viewModel.sendCommand(AddTransactionCommand.ChangePurpose(it)) },
        onValueChange = { viewModel.sendCommand(AddTransactionCommand.ChangeValue(it)) },
        onDateSelect = { viewModel.sendCommand(AddTransactionCommand.ChangeDate(it ?: 0L)) },
        onAdd = {
            viewModel.sendCommand(
                AddTransactionCommand.Save(
                    recordId = state.record?.id!!,
                    purpose = state.purpose,
                    value = state.value,
                    date = state.date,
                )
            )
        },
    )
}