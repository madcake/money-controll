package shiny.mc.feature.periods

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.title_periods
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.domain.value.Period
import shiny.mc.platform.format
import shiny.mc.theme.components.ColumnItem
import shiny.mc.theme.components.ColumnItemValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodsNavScreen(
    onCancel: () -> Unit,
    viewModel: PeriodsViewModel = koinViewModel(),
) {
    val periods by viewModel.periods.collectAsStateWithLifecycle()

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
                    IconButton(onClick = viewModel::newPeriod) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }
            )
        },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(periods) { item ->
                PeriodItemView(
                    period = item,
                ) { viewModel.setCurrent(item) }
            }
        }
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
private fun PeriodItemView(period: Period, onClick: () -> Unit) {
    val title = period.let {
        val date = LocalDate(
            year = period.date.year,
            month = period.date.month,
            day = 1,
        )
        LocalDate.Format {
            monthName(MonthNames.ENGLISH_FULL) // Outputs "January", "February", etc.
            char(' ')
            year()
        }.format(date)
    }
    ColumnItem(
        headline = title,
        trailing = {
            ColumnItemValue(
                value = period.values.assetScheduled.format(),
                supportValue = period.values.liabilityScheduled.format()
            )
        },
        onClick = onClick,
    )
}