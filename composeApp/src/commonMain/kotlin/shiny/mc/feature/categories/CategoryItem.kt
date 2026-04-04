package shiny.mc.feature.categories

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import shiny.mc.core.domain.entity.Category
import shiny.mc.feature.add_category.icon
import shiny.mc.theme.components.ColumnItem

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onMenu: () -> Unit,
    onClick: () -> Unit
) {
    ColumnItem(
        containerColor = if (isSelected) {
            MaterialTheme.colorScheme.inversePrimary
        } else {
            Color.Transparent
        },
        leading = { Icon(category.type.icon, contentDescription = null) },
        headline = category.title,
        trailing = {
            IconButton(onClick = onMenu) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "")
            }
        },
        onMenu = null,
        onClick = onClick,
    )
}