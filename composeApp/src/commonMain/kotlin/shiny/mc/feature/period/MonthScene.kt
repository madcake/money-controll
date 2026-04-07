package shiny.mc.feature.period

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.feature.period.model.ValueState

@Composable
fun MonthScene(
    month: String,
    year: String,
    items: List<RecordItem>,
    onRecord: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "$month $year"
            )
            IconButton(
                onClick = {},
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add category to period"
                )
            }
        }
        RecordsScene(
            items = items,
            onRecord = onRecord,
        )
    }
}

@Preview
@Composable
fun MonthScenePreview() {
    class Item(override val id: String) : RecordItem {
        override val title: String = "Record Item #$id"
        override val scheduledValue: String = "${(id.toInt() + 1000) * 34}"
        override val realValue: String = "${(id.toInt() + 1108) * 34}"
        override val valueState: ValueState = ValueState.Surplus
    }
    MaterialTheme {
        MonthScene(
            month = "April",
            year = "2026",
            items = listOf(
                Item("1"),
                Item("2"),
                Item("3"),
                Item("4"),
                Item("5"),
                Item("10"),
            ),
            onRecord = {}
        )
    }
}