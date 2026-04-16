package shiny.mc.core.coordinators.category

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.dto.Category
import shiny.mc.core.ports.category.SearchCategories

class SearchCategoriesImpl(
    private val categoryRepository: CategoryRepository,
) : SearchCategories {

    override fun searchCategories(query: String): Flow<List<Category>> {
        return categoryRepository.find(query)
    }
}