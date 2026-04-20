package shiny.mc.feature.category.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import shiny.mc.core.domain.entity.CategoryInfo
import shiny.mc.core.dto.Category
import shiny.mc.core.dto.CategoryType

class CategoryInfoImpl(
    override val id: Long,
    override val title: String,
    override val type: CategoryType,
    override val icon: Any
) : CategoryInfo {

    constructor(category: Category) : this(
        id = category.id,
        title = category.title,
        type = category.type,
        icon = when (category.type) {
            CategoryType.In -> Icons.Default.ArrowDropDown
            CategoryType.Out -> Icons.Default.ArrowDropUp
        }
    )
}