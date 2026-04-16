package shiny.mc.core.coordinators.category

import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.dto.Category
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.ports.category.AddCategory

class AddCategoryImpl(
    private val categoryRepository: CategoryRepository,
) : AddCategory {
    override suspend fun addCategory(
        title: String,
        type: CategoryType
    ): Boolean {
        categoryRepository.addCategory(
            Category(
                id = null,
                title = title,
                type = type,
            )
        )
        return true
    }
}