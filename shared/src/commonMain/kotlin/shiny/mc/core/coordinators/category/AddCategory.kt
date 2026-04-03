package shiny.mc.core.coordinators.category

import shiny.mc.core.domain.value.CategoryType

interface AddCategory {
    suspend fun addCategory(title: String, type: CategoryType): Boolean
}