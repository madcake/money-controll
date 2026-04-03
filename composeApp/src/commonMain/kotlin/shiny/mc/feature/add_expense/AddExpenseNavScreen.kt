package shiny.mc.feature.add_expense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.common_save
import moneycontroll.composeapp.generated.resources.placeholders_add_expense_title
import moneycontroll.composeapp.generated.resources.placeholders_add_expense_value
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddExpenseNavScreen(
    viewModel: AddExpenseViewModel =  koinViewModel()
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val formContent = @Composable {
        OutlinedTextField(
            state = viewModel.titleState,
            placeholder = { Text(stringResource(Res.string.placeholders_add_expense_title)) }
        )
        OutlinedTextField(
            state = viewModel.valueState,
            placeholder = { Text(stringResource(Res.string.placeholders_add_expense_value)) }
        )
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (windowSizeClass.minWidthDp < WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) {
            CompactForm(formContent)
        } else {
            ExpandedForm(formContent)
        }

        Button(
            onClick = { viewModel.onSave() }
        ) {
            Text(stringResource(Res.string.common_save))
        }
    }
}

@Composable
private fun CompactForm(content: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
private fun ExpandedForm(content: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}