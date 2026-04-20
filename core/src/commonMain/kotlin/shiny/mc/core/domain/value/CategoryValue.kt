package shiny.mc.core.domain.value

import shiny.mc.core.dto.CategoryType

data class CategoryValue(
    val title: String,
    val type: CategoryType
)