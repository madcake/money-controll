package shiny.mc.feature.category.ports

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.ports.category.DeleteCategory

class DeleteCategoryImpl(
    private val categoryRepository: CategoryRepository,
) : DeleteCategory {
    override suspend fun deleteCategory(categoryId: Long) = withContext(Dispatchers.IO) {
        try {
            categoryRepository.deleteCategory(categoryId)
        } catch (_: Throwable) {}
    }
}