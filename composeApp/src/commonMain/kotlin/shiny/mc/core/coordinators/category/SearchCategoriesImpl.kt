package shiny.mc.core.coordinators.category

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Singleton
import shiny.mc.core.domain.entity.Category
import shiny.mc.core.repositories.CategoryRepository

class SearchCategoriesImpl(
    private val categoryRepository: CategoryRepository,
) : SearchCategories {

    override fun searchCategories(query: String): Flow<List<Category>> {
        return categoryRepository.find(query)
    }
}