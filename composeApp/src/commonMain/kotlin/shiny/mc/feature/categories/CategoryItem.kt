package shiny.mc.feature.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import shiny.mc.core.domain.entity.Category
import shiny.mc.feature.add_category.icon
import shiny.mc.theme.components.ColumnItem

@Composable
fun CategoryItem(category: Category, onMenu: () -> Unit) {
    ColumnItem(
        leading = { Icon(category.type.icon, contentDescription = null) },
        headline = category.title,
        trailing = {
            IconButton(onClick = onMenu) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "")
            }
        },
//        onMenu = onMenu,
    ) {

    }
}