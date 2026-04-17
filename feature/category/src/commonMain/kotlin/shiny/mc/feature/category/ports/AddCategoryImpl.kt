package shiny.mc.feature.category.ports

import kotlinx.coroutines.flow.firstOrNull
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.domain.entity.validate
import shiny.mc.core.dto.Category
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.error.CategoryError
import shiny.mc.core.ports.category.AddCategory

class AddCategoryImpl(
    private val categoryRepository: CategoryRepository,
) : AddCategory {
    override suspend fun addCategory(
        title: String,
        type: CategoryType
    ): Boolean {
        val category = Category(
            title = title,
            type = type
        )
        category.validate()

        if (categoryRepository.getCategory(title).firstOrNull() != null) {
            throw CategoryError.DuplicatedTitle()
        }
        categoryRepository.addCategory(category)
        return true
    }
}