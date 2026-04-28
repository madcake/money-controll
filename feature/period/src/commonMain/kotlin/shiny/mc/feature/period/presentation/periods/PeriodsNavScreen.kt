package shiny.mc.feature.period.presentation.periods

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.domain.value.format
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.error.PeriodError
import shiny.mc.core_ui.components.ColumnItem
import shiny.mc.core_ui.components.ColumnItemValue
import shiny.mc.core_ui.components.ItemPosition
import shiny.mc.core_ui.components.MonthPicker
import shiny.mc.core_ui.components.SwipeableItem
import shiny.mc.core_ui.components.itemsPosition
import shiny.mc.core_ui.model.CommandState
import shiny.mc.core_ui.resources.Res
import shiny.mc.core_ui.resources.common_error
import shiny.mc.core_ui.resources.common_error_unknown
import shiny.mc.core_ui.resources.common_ok
import shiny.mc.core_ui.resources.error_period_add_exists
import shiny.mc.core_ui.resources.error_period_add_not_found
import shiny.mc.core_ui.resources.error_title_add
import shiny.mc.core_ui.resources.error_title_copy
import shiny.mc.core_ui.resources.title_periods
import shiny.mc.core_ui.theme.bullish
import shiny.mc.core_ui.theme.space
import shiny.mc.feature.period.presentation.add_period.AddPeriodViewModel
import shiny.mc.platform.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodsNavScreen(
    onCancel: () -> Unit,
    viewModel: PeriodsViewModel = koinViewModel(),
    addPeriodViewModel: AddPeriodViewModel = koinViewModel(),
) {
    val periods by viewModel.periods.collectAsStateWithLifecycle()
    val current by viewModel.current.collectAsStateWithLifecycle()

    val copyPeriodState by addPeriodViewModel.copyPeriodState.collectAsStateWithLifecycle()
    val newEmptyPeriodState by addPeriodViewModel.newEmptyPeriodState.collectAsStateWithLifecycle()

    var selectDate by remember { mutableStateOf(false) }
    var periodRevealed by remember { mutableStateOf<PeriodDate?>(null) }
    var showError by remember { mutableStateOf<Throwable?>(null) }

    val onCancel = fun() {
        selectDate = false
        periodRevealed = null
        showError = null
        onCancel()
    }

    LaunchedEffect(copyPeriodState) {
        when (copyPeriodState) {
            is CommandState.Success<*> -> onCancel()
            is CommandState.Failure<*> -> showError = (copyPeriodState as CommandState.Failure<*>).err
            is CommandState.Idle<*> -> showError = null
            is CommandState.Processing<*> -> {}
        }
    }

    LaunchedEffect(newEmptyPeriodState) {
        when (newEmptyPeriodState) {
            is CommandState.Success<*> -> onCancel()
            is CommandState.Failure<*> -> showError = (newEmptyPeriodState as CommandState.Failure<*>).err
            is CommandState.Idle<*> -> showError = null
            is CommandState.Processing<*> -> {}
        }
    }

    DisposableEffect(Unit) {
        onDispose { addPeriodViewModel.reset()}
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
                    onCopy = { selectDate = true }
                ) {
                    viewModel.setCurrent(item)
                    onCancel()
                }
            }
        }
    }

    SelectPeriod(
        selectDate = selectDate,
        selectTo = periodRevealed,
        onCancel = {
            selectDate = false
            periodRevealed = null
        },
    ) { month, year, from ->
        when {
            from != null -> addPeriodViewModel.copyPeriod(from, PeriodDate(month, year))
            else -> addPeriodViewModel.newEmptyPeriod(PeriodDate(month, year))
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
                val text = when {
                    newEmptyPeriodState is CommandState.Failure -> stringResource(Res.string.error_title_add)
                    copyPeriodState is CommandState.Failure -> stringResource(Res.string.error_title_copy)
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
    selectTo: PeriodDate?,
    onCancel: () -> Unit,
    onSelect: (Int, Int, PeriodDate?) -> Unit,
) {
    MonthPicker(
        visible = selectDate || selectTo != null,
        onSelect = { month, year -> onSelect(month, year, selectTo) },
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
                    tint = MaterialTheme.colorScheme.bullish,
                    contentDescription = "",
                )
            }
        },
        itemPosition = position,
        onReveal = onReveal,
    ) {
        ColumnItem(
            headline = period.date.format(),
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