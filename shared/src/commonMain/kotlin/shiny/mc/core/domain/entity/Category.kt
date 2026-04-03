package shiny.mc.core.domain.entity

import shiny.mc.core.domain.value.CategoryType

data class Category(
    val id: Long?,
    val title: String,
    val type: CategoryType,
)