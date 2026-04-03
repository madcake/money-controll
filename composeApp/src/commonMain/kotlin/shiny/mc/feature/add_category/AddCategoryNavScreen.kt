package shiny.mc.feature.add_category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddCategoryNavScreen(
    viewModel: AddCategoryViewModel = koinViewModel()
) {
    val categoryType by viewModel.type.collectAsStateWithLifecycle()
    val isInputValid by viewModel.inputValid.collectAsStateWithLifecycle()
    val commandState by viewModel.commandState.collectAsStateWithLifecycle()

    AddCategoryScene(
        title = viewModel.titleState,
        categoryType = categoryType,
        commandState = commandState,
        onCategoryTypeSelected = viewModel::onCategoryTypeSelected,
        onSave = viewModel::onSave
    )
}