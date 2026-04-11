package shiny.mc.feature.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import shiny.mc.core.domain.entity.Category
import shiny.mc.feature.add_category.icon
import shiny.mc.theme.components.ColumnItem
import shiny.mc.theme.components.ItemPosition

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    position: ItemPosition,
    onMenu: () -> Unit,
    onClick: () -> Unit
) {
    ColumnItem(
        leading = {
            Icon(category.type.icon, contentDescription = null)
        },
        headline = category.title,
        trailing = {
            if (isSelected) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")
            }
        },
        position = position,
        onMenu = null,
        onClick = onClick,
    )
}