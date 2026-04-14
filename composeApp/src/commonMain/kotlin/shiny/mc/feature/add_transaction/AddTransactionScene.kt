package shiny.mc.feature.add_transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.error_transaction_empty_purpose
import moneycontroll.composeapp.generated.resources.error_transaction_invalid_value
import moneycontroll.composeapp.generated.resources.error_transaction_unknown_error
import moneycontroll.composeapp.generated.resources.placeholders_add_expense_value
import moneycontroll.composeapp.generated.resources.placeholders_add_transaction_purpose
import org.jetbrains.compose.resources.stringResource
import shiny.mc.core.domain.value.TransactionError
import shiny.mc.core.model.CommandState
import shiny.mc.theme.components.SmallCircularProgressIndicator
import shiny.mc.theme.paddingDefault
import shiny.mc.theme.space
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddTransactionScene(
    recordMonth: Int,
    recordYear: Int,
    state: CommandState<AddTransactionCommand>,
    value: TextFieldState,
    purpose: TextFieldState,
    date: Long,
    purposeSuggestions: List<String>,
    onDateSelect: (Long?) -> Unit,
    onAdd: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val valueFocusRequester = remember { FocusRequester() }

    var transactionSuggestionShowed by remember { mutableStateOf(false) }
    val isProcessing = state is CommandState.Processing

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date,
        yearRange = IntRange(recordYear, recordYear),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Instant.fromEpochMilliseconds(utcTimeMillis).toLocalDateTime(TimeZone.UTC)
                return recordYear == date.year && recordMonth == date.month.number
            }
        },
    )
    val selectedDate = datePickerState.selectedDateMillis?.let { timeMillis ->
        Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault()).let {
            Pair(it.day, it.month)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .paddingDefault(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraSmall),
            expanded = transactionSuggestionShowed,
            onExpandedChange = { transactionSuggestionShowed = !transactionSuggestionShowed }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
                state = purpose,
                placeholder = { Text(stringResource(Res.string.placeholders_add_transaction_purpose)) },
                enabled = !isProcessing,
                lineLimits = TextFieldLineLimits.MultiLine(1, 6),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                onKeyboardAction = {
                    focusManager.moveFocus(FocusDirection.Next)
                },
            )
            ExposedDropdownMenu(
                expanded = transactionSuggestionShowed,
                onDismissRequest = { transactionSuggestionShowed = false },
            ) {
                purposeSuggestions.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item)
                            }
                        },
                        onClick = {
                            purpose.clearText()
                            purpose.setTextAndPlaceCursorAtEnd(item)
                            valueFocusRequester.requestFocus()
                            transactionSuggestionShowed = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .clickable(
                        onClick = { showDatePicker = true },
                        enabled = !isProcessing,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "")
                Text(selectedDate?.let { "${it.first} ${it.second}" } ?: "None")
            }
            OutlinedTextField(
                modifier = Modifier.weight(0.6f).focusRequester(valueFocusRequester),
                state = value,
                placeholder = { Text(stringResource(Res.string.placeholders_add_expense_value)) },
                enabled = !isProcessing,
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal,
                ),
                onKeyboardAction = { onAdd() },
                trailingIcon = {
                    IconButton(
                        onClick = onAdd
                    ) {
                        if (isProcessing) {
                            SmallCircularProgressIndicator()
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Send,
                                contentDescription = ""
                            )
                        }
                    }
                },
            )
        }
        AddTransactionFailure(state)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        onDateSelect(datePickerState.selectedDateMillis)
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AddTransactionFailure(state: CommandState<AddTransactionCommand>) {
    if (state !is CommandState.Failure) {
        return
    }
    val message = when (state.err) {
        is TransactionError.IncorrectValue -> stringResource(Res.string.error_transaction_invalid_value)
        is TransactionError.IncorrectPurpose -> stringResource(Res.string.error_transaction_empty_purpose)
        else -> stringResource(Res.string.error_transaction_unknown_error, state.err.message ?: "")
    }
    Text(
        modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.space.paddingDefault),
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmallEmphasized
    )
}

@Preview
@Composable
fun AddTransactionScenePreview() {
    MaterialTheme {
        AddTransactionScene(
            recordMonth = 4,
            recordYear = 2026,
            state = CommandState.Idle(),
            value = rememberTextFieldState(),
            purpose = rememberTextFieldState(),
            date = Clock.System.now().toEpochMilliseconds(),
            purposeSuggestions = listOf(
                "Transaction test 1",
                "Transaction test 2",
                "Transaction test 3",
            ),
            onDateSelect = {},
            onAdd = {},
        )
    }
}