package shiny.mc.feature.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import shiny.mc.feature.add_expense.AddExpenseNavScreen
import shiny.mc.feature.records.components.RecordItemView
import shiny.mc.feature.records.model.RecordItem
import shiny.mc.feature.records.model.ValueState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesListNavScreen(
    onCategories: () -> Unit,
    viewModel: ExpensesListViewModel = koinViewModel<ExpensesListViewModel>()
) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Expenses") },
                actions = {
                    IconButton(
                        onClick = onCategories
                    ) {
                        Icon(imageVector = Icons.Default.Category, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                AddExpenseNavScreen()
            }
            items(items) { item ->
                Column {
                    Row {
                        Column {
                            Text(item.title)
                            Text(item.description)
                        }
                        Column {
                            Text("${item.plannedValue}")
                            Text("${item.realValue}")
                        }
                    }
                    Text(item.type.name)
                }
            }
        }
    }
}

@Composable
fun RecordsScene(
    items: List<RecordItem>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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
    class Item(override val id: Int) : RecordItem {
        override val title: String = "Record Item #$id"
        override val scheduledValue: String = "${(id + 1000) * 34}"
        override val realValue: String = "${(id + 1108) * 34}"
        override val valueState: ValueState = ValueState.Surplus
    }

    MaterialTheme {
        RecordsScene(
            items = listOf(
                Item(1),
                Item(2),
                Item(3),
                Item(4),
                Item(5),
                Item(10),
            )
        )
    }
}