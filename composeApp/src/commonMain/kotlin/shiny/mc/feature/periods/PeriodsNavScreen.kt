package shiny.mc.feature.periods

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.title_periods
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.domain.value.Period
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
) {
    val periods by viewModel.periods.collectAsStateWithLifecycle()
    var visibleMonthPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onCancel) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "")
                    }
                },
                title = { Text(stringResource(Res.string.title_periods)) },
                actions = {
                    IconButton(
                        onClick = { visibleMonthPicker = true }
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
                    position = position,
                ) {
                    viewModel.setCurrent(item)
                    onCancel()
                }
            }
        }
    }

    MonthPicker(
        visible = visibleMonthPicker,
        onSelect = { month, year ->
            viewModel.newPeriod(month, year)
            onCancel()
        },
        onCancel = { visibleMonthPicker = false }
    )
}

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
private fun PeriodItemView(
    period: Period,
    position: ItemPosition,
    onClick: () -> Unit
) {
    ColumnItem(
        headline = period.date.toString(),
        trailing = {
            ColumnItemValue(
                value = period.values.assetScheduled.format(),
                supportValue = period.values.liabilityScheduled.format()
            )
        },
        position = position,
        onClick = onClick,
    )
}