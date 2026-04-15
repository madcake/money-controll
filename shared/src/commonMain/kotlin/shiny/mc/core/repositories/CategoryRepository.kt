package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.Category

interface CategoryRepository {
    /**
     * @return available categories from store
     */
    fun getCategories(): Flow<List<Category>>

    /**
     * Add category to store
     *
     * @param category to add
     * @return added category
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    suspend fun addCategory(category: Category): Category

    /**
     * Delete category by category id
     *
     * @param category id
     * @throws shiny.mc.core.domain.value.CategoryError.CategoryNotFound
     */
    suspend fun deleteCategory(id: Long)

    /**
     * Update category
     *
     * @param category category
     * @throws shiny.mc.core.domain.value.CategoryError.CategoryNotFound
     */
    suspend fun updateCategory(category: Category)

    /**
     * Get category by id
     *
     * @return category, null if not found
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getCategory(id: Long): Flow<Category?>

    /**
     * Get category by title
     *
     * @param title category title
     * @return category, null if not found
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getCategory(title: String): Flow<Category?>

    /**
     * Find categories by query string.
     *
     * @param query Query string could be empty
     * @return categories
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun find(query: String): Flow<List<Category>>
}

