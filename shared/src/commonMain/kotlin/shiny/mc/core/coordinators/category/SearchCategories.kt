package shiny.mc.core.coordinators.category

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.Category

interface SearchCategories {
    fun searchCategories(query: String): Flow<List<Category>>
}