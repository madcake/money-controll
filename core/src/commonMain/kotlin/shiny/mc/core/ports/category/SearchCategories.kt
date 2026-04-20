package shiny.mc.core.ports.category

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.CategoryInfo
import shiny.mc.core.dto.Category

/**
 * Port for searching through transaction categories.
 */
interface SearchCategories {
    /**
     * Searches for categories that match the given query string.
     *
     * @param query The search term to filter categories by title.
     * @return A [Flow] emitting a list of [CategoryInfo] matching the query.
     */
    fun searchCategories(query: String): Flow<List<CategoryInfo>>
}