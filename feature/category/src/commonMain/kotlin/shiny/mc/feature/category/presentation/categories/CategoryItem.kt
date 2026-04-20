package shiny.mc.feature.category.presentation.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import shiny.mc.core.domain.entity.CategoryInfo
import shiny.mc.core.dto.Category
import shiny.mc.core_ui.components.ColumnItem
import shiny.mc.core_ui.components.ItemPosition
import shiny.mc.feature.category.presentation.add_category.icon

@Composable
fun CategoryItem(
    category: CategoryInfo,
    isSelected: Boolean,
    position: ItemPosition,
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
        onClick = onClick,
    )
}