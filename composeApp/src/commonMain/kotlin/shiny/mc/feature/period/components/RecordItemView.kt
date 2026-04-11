package shiny.mc.feature.period.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shiny.mc.feature.period.model.RecordItem
import shiny.mc.feature.period.model.ValueState
import shiny.mc.theme.components.ColumnItem
import shiny.mc.theme.components.ColumnItemValue
import shiny.mc.theme.components.ItemPosition

@Composable
fun RecordItemView(
    item: RecordItem,
    position: ItemPosition,
    onClick: () -> Unit,
) {
    ColumnItem(
        headline = item.title,
        trailing = {
            ColumnItemValue(
                value = item.scheduledValue,
                supportValue = item.realValue,
                supportColor = when (item.valueState) {
                    ValueState.Deficit -> MaterialTheme.colorScheme.error
                    ValueState.Surplus -> MaterialTheme.colorScheme.secondary
                }
            )
        },
        position = position,
        onClick = onClick,
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
            },
            position = ItemPosition.Single,
            onClick = {},
        )
    }
}