package shiny.mc.feature.period.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.feature.period.model.ValueState
import shiny.mc.theme.components.ColumnItem
import shiny.mc.theme.components.ColumnItemValue

@Composable
fun RecordItemView(
    item: RecordItem,
) {
    ColumnItem(
        headline = item.title,
        trailing = { ColumnItemValue(item.scheduledValue, item.realValue) },
        onClick = {}
    )
}

@Preview
@Composable
fun RecordItemViewPreview() {
    MaterialTheme {
        RecordItemView(
            item = object : RecordItem {
                override val id: String = "1"
                override val title: String = "Foo category version 1.0 for preview"
                override val scheduledValue: String = "150 000"
                override val realValue: String = "170 000"
                override val valueState: ValueState = ValueState.Surplus
            }
        )
    }
}