package shiny.mc.feature.record

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import shiny.mc.platform.format
import shiny.mc.theme.space

@Composable
internal fun RecordHeader(
    recordId: String,
    viewModel: EditRecordViewModel = koinViewModel(key = recordId, parameters = { parametersOf(recordId) }),
) {
    val record by viewModel.record.collectAsStateWithLifecycle()

    var editable by remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()
    LaunchedEffect(editable) {
        if (editable) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.space.paddingDefault),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (editable) {
            BasicTextField(
                modifier = Modifier.focusRequester(focusRequester)
                    .width(IntrinsicSize.Min),
                state = viewModel.scheduleValueState,
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = MaterialTheme.typography.displaySmall.copy(
                    color = if (viewModel.scheduleValueState.text.isEmpty()) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,

                    ),
                onKeyboardAction = {
                    editable = false
                    viewModel.update()
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                outputTransformation = OutputTransformation {
                    if (this.length == 0) {
                        this.append("0")
                    }
                }
            )
        } else {
            Text(
                modifier = Modifier.clickable { editable = true },
                text = viewModel.scheduleValueState.text.toString().toDoubleOrNull()?.format() ?: "0.0",
                style = MaterialTheme.typography.displaySmall.copy(
                    color = if (viewModel.scheduleValueState.text.isEmpty()) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                ),
            )
        }
        Text(
            text = record?.realValue?.format() ?: "",
            style = MaterialTheme.typography.displaySmall.copy(
                textAlign = TextAlign.Center,
                color = if ((record?.scheduledValue ?: 0.0) < (record?.realValue ?: 0.0)) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.secondary
                }
            ),
        )
    }
}