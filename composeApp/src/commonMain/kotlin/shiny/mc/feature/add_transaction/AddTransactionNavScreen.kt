package shiny.mc.feature.add_transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddTransactionNavScreen(
    onCancel: () -> Unit,
    recordId: String,
    viewModel: AddTransactionViewModel = koinViewModel { parametersOf(recordId) }
) {
    val date by viewModel.date.collectAsStateWithLifecycle()
    val commandState by viewModel.commandState.collectAsStateWithLifecycle()

    AddTransactionScene(
        state = commandState,
        value = viewModel.valueState,
        purpose = viewModel.purposeState,
        date = date,
        onDateSelect = viewModel::date,
        onAdd = viewModel::add,
    )
}