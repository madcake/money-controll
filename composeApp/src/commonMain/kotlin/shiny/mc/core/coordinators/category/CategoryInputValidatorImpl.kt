package shiny.mc.core.coordinators.category

import kotlinx.coroutines.flow.firstOrNull
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.error.CategoryError
import shiny.mc.core.ports.category.CategoryInputValidator

class CategoryInputValidatorImpl(
    private val categoryRepository: CategoryRepository,
) : CategoryInputValidator {
    override suspend fun validateCategoryInput(
        title: String?,
        type: CategoryType?
    ): Boolean {
        when {
            title.isNullOrEmpty() -> throw CategoryError.EmptyTitle()
            isDuplicated(title) -> throw CategoryError.DuplicatedTitle()
        }
        return true
    }

    private suspend fun isDuplicated(title: String): Boolean =
        categoryRepository.getCategory(title).firstOrNull() != null

}