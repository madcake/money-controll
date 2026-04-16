package shiny.mc.core.ports.category

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Category

interface SearchCategories {
    fun searchCategories(query: String): Flow<List<Category>>
}