package shiny.mc.feature.category.ports

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.domain.entity.CategoryInfo
import shiny.mc.core.ports.category.SearchCategories
import shiny.mc.feature.category.domain.CategoryInfoImpl

@OptIn(ExperimentalCoroutinesApi::class)
class SearchCategoriesImpl(
    private val categoryRepository: CategoryRepository,
) : SearchCategories {

    override fun searchCategories(query: String): Flow<List<CategoryInfo>> {
        return categoryRepository.find(query).mapLatest { items ->
            items.map { CategoryInfoImpl(it) }
        }
    }
}