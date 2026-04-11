package shiny.mc.feature.period

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.period_edit
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.feature.period.components.RecordItemView
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.feature.period.model.ValueState
import shiny.mc.theme.components.itemsPosition
import shiny.mc.theme.space

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodNavScreen(
    onCategories: (PeriodDate) -> Unit,
    onRecord: (String) -> Unit,
    onPeriods: () -> Unit,
    viewModel: PeriodViewModel = koinViewModel<PeriodViewModel>()
) {
    val period by viewModel.period.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val items by viewModel.records.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.clickable(onClick = onPeriods),
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
            items = items,
            onRecord = onRecord,
        )
    }
}

@Composable
fun RecordsScene(
    items: List<RecordItem>,
    onRecord: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.space.paddingDefault),
        verticalArrangement = MaterialTheme.space.dividerArrangement,
    ) {
        itemsPosition(items, key = { _, item -> item.id }) { position, item ->
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
            items = listOf(
                Item("0"),
                Item("2"),
                Item("3"),
                Item("4"),
                Item("5"),
                Item("10"),
            ),
            onRecord = {},
        )
    }
}