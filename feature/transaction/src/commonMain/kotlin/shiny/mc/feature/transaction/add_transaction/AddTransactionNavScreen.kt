package shiny.mc.feature.transaction.add_transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.update
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddTransactionNavScreen(
    onCancel: () -> Unit,
    recordId: String,
    viewModel: AddTransactionViewModel = koinViewModel()//(key = recordId) { parametersOf(recordId) },
) {
    DisposableEffect(recordId) {

        viewModel.recordId.update { recordId }

        onDispose {
            viewModel.reset()
        }
    }

    val date by viewModel.date.collectAsStateWithLifecycle()
    val commandState by viewModel.commandState.collectAsStateWithLifecycle()
    val record by viewModel.record.collectAsStateWithLifecycle()
    val purposeSuggestions by viewModel.purposeSuggestions.collectAsStateWithLifecycle()

    if (record == null) {
        return
    }
    AddTransactionScene(
        recordMonth = record?.month ?: 0,
        recordYear = record?.year ?: 0,
        state = commandState,
        value = viewModel.valueState,
        purpose = viewModel.purposeState,
        purposeSuggestions = purposeSuggestions,
        date = date,
        onDateSelect = viewModel::date,
        onAdd = { viewModel.add(recordId) },
    )
}