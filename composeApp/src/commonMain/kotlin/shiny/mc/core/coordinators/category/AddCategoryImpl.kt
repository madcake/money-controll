package shiny.mc.core.coordinators.category

import org.koin.core.annotation.Singleton
import shiny.mc.core.domain.entity.Category
import shiny.mc.core.domain.value.CategoryType
import shiny.mc.core.repositories.CategoryRepository

@Singleton
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