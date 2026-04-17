package shiny.mc.core.domain.entity

import shiny.mc.core.dto.CategoryType

interface CategoryInfo {
    val id: Long
    val title: String
    val type: CategoryType
    val icon: Any
}