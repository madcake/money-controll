package shiny.mc.core.coordinators.category

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import shiny.mc.core.repositories.CategoryRepository

class DeleteCategoryImpl(
    private val categoryRepository: CategoryRepository,
) : DeleteCategory {
    override suspend fun deleteCategory(categoryId: Long) = withContext(Dispatchers.IO) {
        try {
            categoryRepository.deleteCategory(categoryId)
        } catch (_: Throwable) {}
    }
}