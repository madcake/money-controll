package shiny.mc.feature.periods

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.common_error
import moneycontroll.composeapp.generated.resources.common_error_unknown
import moneycontroll.composeapp.generated.resources.common_ok
import moneycontroll.composeapp.generated.resources.error_period_add_exists
import moneycontroll.composeapp.generated.resources.error_period_add_not_found
import moneycontroll.composeapp.generated.resources.error_title_add
import moneycontroll.composeapp.generated.resources.error_title_copy
import moneycontroll.composeapp.generated.resources.title_periods
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.error.PeriodError
import shiny.mc.core.model.CommandState
import shiny.mc.feature.add_period.AddPeriodViewModel
import shiny.mc.feature.add_period.model.AddPeriodCommand
import shiny.mc.feature.record.SwipeableItem
import shiny.mc.platform.format
import shiny.mc.theme.components.ColumnItem
import shiny.mc.theme.components.ColumnItemValue
import shiny.mc.theme.components.ItemPosition
import shiny.mc.theme.components.MonthPicker
import shiny.mc.theme.components.itemsPosition
import shiny.mc.theme.space

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodsNavScreen(
    onCancel: () -> Unit,
    viewModel: PeriodsViewModel = koinViewModel(),
    addPeriodViewModel: AddPeriodViewModel = koinViewModel(),
) {
    val periods by viewModel.periods.collectAsStateWithLifecycle()
    val current by viewModel.current.collectAsStateWithLifecycle()
    val commandState by addPeriodViewModel.commandState.collectAsStateWithLifecycle()

    var selectDate by remember { mutableStateOf(false) }
    var periodRevealed by remember { mutableStateOf<PeriodDate?>(null) }
    var showError by remember { mutableStateOf<Throwable?>(null) }

    val onCancel = fun() {
        selectDate = false
        periodRevealed = null
        showError = null
        onCancel()
    }

    LaunchedEffect(commandState) {
        when (commandState) {
            is CommandState.Success<*> -> onCancel()
            is CommandState.Failure -> showError = (commandState as CommandState.Failure<*>).err
            is CommandState.Idle -> showError = null
            is CommandState.Processing -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onCancel) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "")
                    }
                },
                title = {
                    Text(stringResource(Res.string.title_periods))
                },
                actions = {
                    IconButton(
                        onClick = { selectDate = true }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            contentPadding = PaddingValues(MaterialTheme.space.paddingDefault),
            verticalArrangement = MaterialTheme.space.dividerArrangement
        ) {
            itemsPosition(periods) { position, item ->
                PeriodItemView(
                    period = item,
                    current = item.date == current,
                    position = position,
                    isRevealed = item.date == periodRevealed,
                    onReveal = { state -> periodRevealed = item.date.takeIf { state } },
                    onCopy = {
                        addPeriodViewModel.copy(item.date)
                    }
                ) {
                    viewModel.setCurrent(item)
                    onCancel()
                }
            }
        }
    }

    SelectPeriod(
        selectDate = selectDate,
        selectTo = commandState.command as? AddPeriodCommand.CopySelectTo,
        onCancel = {},
    ) { month, year, from ->
        when {
            selectDate -> addPeriodViewModel.add(PeriodDate(month, year))
            from != null -> addPeriodViewModel.copy(from, PeriodDate(month, year))
        }
        selectDate = false
    }

    if (showError != null) {
        AlertDialog(
            onDismissRequest = { addPeriodViewModel.reset() },
            confirmButton = {
                Button({ addPeriodViewModel.reset() }) {
                    Text(stringResource(Res.string.common_ok))
                }
            },
            title = {
                val text = when (commandState.command) {
                    is AddPeriodCommand.Add -> stringResource(Res.string.error_title_add)
                    is AddPeriodCommand.Copy -> stringResource(Res.string.error_title_copy)
                    else -> stringResource(Res.string.common_error)
                }
                Text(text)
            },
            text = {
                val text = when (showError) {
                    is PeriodError.PeriodExists -> stringResource(Res.string.error_period_add_exists)
                    is PeriodError.PeriodNotFound -> stringResource(Res.string.error_period_add_not_found)
                    else -> showError?.message ?: stringResource(Res.string.common_error_unknown)
                }
                Text(text)
            }
        )
    }
}

@Composable
private fun SelectPeriod(
    selectDate: Boolean,
    selectTo: AddPeriodCommand.CopySelectTo?,
    onCancel: () -> Unit,
    onSelect: (Int, Int, PeriodDate?) -> Unit,
) {
    MonthPicker(
        visible = selectDate || selectTo != null,
        onSelect = { month, year -> onSelect(month, year, selectTo?.from) },
        onCancel = onCancel,
    )
}

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
private fun PeriodItemView(
    period: Period,
    current: Boolean,
    position: ItemPosition,
    isRevealed: Boolean,
    onReveal: (Boolean) -> Unit,
    onCopy: () -> Unit,
    onClick: () -> Unit,
) {
    SwipeableItem(
        isRevealed = isRevealed,
        backgroundContent = {
            IconButton(onCopy) {
                Icon(
                    imageVector = Icons.Default.CopyAll,
                    tint = MaterialTheme.colorScheme.tertiary,
                    contentDescription = "",
                )
            }
        },
        itemPosition = position,
        onReveal = onReveal,
    ) {
        ColumnItem(
            headline = period.date.toString(),
            leading = if (current) {
                { Icon(imageVector = Icons.Default.Check, contentDescription = "Current period") }
            } else {
                null
            },
            trailing = {
                ColumnItemValue(
                    value = period.values.inEstimate.format(),
                    supportValue = period.values.outEstimate.format()
                )
            },
            position = position,
            onClick = onClick,
        )
    }
}