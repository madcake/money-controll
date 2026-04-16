package shiny.mc.feature.period

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.period_edit
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.PeriodValues
import shiny.mc.core.dto.ValueState
import shiny.mc.core.dto.ValueType
import shiny.mc.feature.period.components.NumberColumnView
import shiny.mc.feature.period.components.RecordItemView
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.theme.components.itemsPosition
import shiny.mc.theme.paddingDefault
import shiny.mc.theme.space

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodNavScreen(
    onCategories: (PeriodDate) -> Unit,
    onRecord: (String) -> Unit,
    onPeriods: () -> Unit,
    viewModel: PeriodViewModel = koinViewModel<PeriodViewModel>()
) {
    val period by viewModel.periodDate.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val periodValues by viewModel.periodValues.collectAsStateWithLifecycle()
    val inRecords by viewModel.inRecords.collectAsStateWithLifecycle()
    val outRecords by viewModel.outRecords.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = onPeriods)
                            .padding(MaterialTheme.space.paddingDefault),
                        text = title
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onCategories(period) }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.period_edit),
                            contentDescription = "Edit period"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        RecordsScene(
            modifier = Modifier.padding(innerPadding),
            periodValues = periodValues,
            inRecords = inRecords,
            outRecords = outRecords,
            onRecord = onRecord,
        )
    }
}

@Composable
fun RecordsScene(
    periodValues: PeriodValues?,
    inRecords: List<RecordItem>,
    outRecords: List<RecordItem>,
    onRecord: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.space.paddingDefault),
        verticalArrangement = MaterialTheme.space.dividerArrangement,
    ) {

        periodValues?.let {
            item {
                Row(
                    modifier = Modifier.paddingDefault(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.space.paddingDefault),
                ) {
                    NumberColumnView(
                        modifier = Modifier.weight(0.5f),
                        values = it,
                        type = ValueType.Estimate,
                    )
                    NumberColumnView(
                        modifier = Modifier.weight(0.5f),
                        values = it,
                        type = ValueType.Real,
                    )
                }
            }

        }

        itemsPosition(inRecords, key = { _, item -> item.id }) { position, item ->
            RecordItemView(item, position) {
                onRecord(item.id)
            }
        }
        item { if (inRecords.isNotEmpty()) MaterialTheme.space.groupSpace() }
        itemsPosition(outRecords, key = { _, item -> item.id }) { position, item ->
            RecordItemView(item, position) {
                onRecord(item.id)
            }
        }
    }
}

@Preview
@Composable
fun RecordsScenePreview() {
    class Item(override val id: String) : RecordItem {
        override val title: String = "Record Item #$id"
        override val scheduledValue: String = "${(id.toInt()) * 34}"
        override val realValue: String = "${(id.toInt() + 1108) * 34}"
        override val valueState: ValueState = ValueState.Surplus
    }

    MaterialTheme {
        RecordsScene(
            outRecords = listOf(
                Item("0"),
                Item("1"),
                Item("2"),
                Item("3"),
                Item("6"),
                Item("7"),
                Item("8"),
                Item("9"),
            ),
            inRecords = listOf(
                Item("4"),
                Item("5"),
                Item("10"),
            ),
            periodValues = PeriodValues(
                inEstimate = 750_000.0,
                outEstimate = 70_000.0,
                inReal = 50_000.0,
                outReal = 850_000.0
            ),
            onRecord = {},
        )
    }
}