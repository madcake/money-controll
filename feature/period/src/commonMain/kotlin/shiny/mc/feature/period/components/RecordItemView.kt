package shiny.mc.feature.period.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import shiny.mc.core.domain.entity.RecordInfo
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.ValueState
import shiny.mc.core_ui.components.ColumnItem
import shiny.mc.core_ui.components.ColumnItemValue
import shiny.mc.core_ui.components.ItemPosition

@Composable
fun RecordItemView(
    item: RecordInfo,
    position: ItemPosition,
    onClick: () -> Unit,
) {
    ColumnItem(
        headline = item.title,
        trailing = {
            ColumnItemValue(
                value = item.estimateValue,
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
            item = object : RecordInfo {
                override val id: String = "1"
                override val categoryId: Long = 1
                override val categoryType: CategoryType = CategoryType.Out
                override val title: String = "Foo category version 1.0 for preview"
                override val estimateValue: String = "150 000"
                override val realValue: String = "170 000"
                override val valueState: ValueState = ValueState.Surplus
                override val icon: Any = Icons.Default.ArrowDropUp
            },
            position = ItemPosition.Single,
            onClick = {},
        )
    }
}