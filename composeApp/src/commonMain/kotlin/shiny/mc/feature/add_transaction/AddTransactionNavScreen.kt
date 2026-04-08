package shiny.mc.feature.add_transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddTransactionNavScreen(
    onCancel: () -> Unit,
    recordId: String,
    viewModel: AddTransactionViewModel = koinViewModel()//(key = recordId) { parametersOf(recordId) },
) {
    val date by viewModel.date.collectAsStateWithLifecycle()
    val commandState by viewModel.commandState.collectAsStateWithLifecycle()
    val record by viewModel.record(recordId).collectAsStateWithLifecycle()

    if (record == null) { // TODO: Improve it
        return
    }
    AddTransactionScene(
        recordMonth = record?.month ?: 0,
        recordYear = record?.year ?: 0,
        state = commandState,
        value = viewModel.valueState,
        purpose = viewModel.purposeState,
        date = date,
        onDateSelect = viewModel::date,
        onAdd = { viewModel.add(recordId) },
    )
}