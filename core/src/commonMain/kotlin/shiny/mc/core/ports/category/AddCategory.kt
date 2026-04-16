package shiny.mc.core.ports.category

import shiny.mc.core.dto.CategoryType

interface AddCategory {
    suspend fun addCategory(title: String, type: CategoryType): Boolean
}