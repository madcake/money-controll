package shiny.mc.feature.period

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.feature.period.components.RecordItemView
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.feature.period.model.ValueState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodNavScreen(
    onCategories: (Int, Int) -> Unit,
    viewModel: PeriodViewModel = koinViewModel<PeriodViewModel>()
) {
    val period by viewModel.period.collectAsStateWithLifecycle()
    val items by viewModel.records.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(period) },
                actions = {
                    IconButton(
                        onClick = { onCategories(4, 2026) }
                    ) {
                        Icon(imageVector = Icons.Default.Category, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        RecordsScene(
            modifier = Modifier.padding(innerPadding),
            items = items,
        )
    }
}

@Composable
fun RecordsScene(
    items: List<RecordItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it.id }) { item ->
            RecordItemView(item)
        }
    }
}

@Preview
@Composable
fun RecordsScenePreview() {
    class Item(override val id: String) : RecordItem {
        override val title: String = "Record Item #$id"
        override val scheduledValue: String = "${(id.toInt() + 1000) * 34}"
        override val realValue: String = "${(id.toInt() + 1108) * 34}"
        override val valueState: ValueState = ValueState.Surplus
    }

    MaterialTheme {
        RecordsScene(
            items = listOf(
                Item("1"),
                Item("2"),
                Item("3"),
                Item("4"),
                Item("5"),
                Item("10"),
            )
        )
    }
}