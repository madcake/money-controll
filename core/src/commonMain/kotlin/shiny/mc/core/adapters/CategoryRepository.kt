package shiny.mc.core.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Category

/**
 * Repository interface for managing transaction categories in persistent storage.
 */
interface CategoryRepository {
    /**
     * Retrieves all available categories.
     *
     * @return A [Flow] emitting a list of all [Category] objects.
     */
    fun getCategories(): Flow<List<Category>>

    /**
     * Adds a new category to the store.
     *
     * @param category The category to add.
     * @return The added category
     * @throws shiny.mc.core.dto.error.CategoryError If the category is invalid or a duplicate.
     */
    suspend fun addCategory(category: Category): Category

    /**
     * Deletes a category by its ID.
     *
     * @param id The unique identifier of the category to delete.
     * @throws shiny.mc.core.dto.error.CategoryError.CategoryNotFound If no category exists with the given ID.
     */
    suspend fun deleteCategory(id: Long)

    /**
     * Updates an existing category.
     *
     * @param category The category with updated information.
     * @throws shiny.mc.core.dto.error.CategoryError.CategoryNotFound If the category to update does not exist.
     */
    suspend fun updateCategory(category: Category)

    /**
     * Retrieves a category by its ID.
     *
     * @param id The unique identifier of the category.
     * @return A [Flow] emitting the category, or null if not found.
     */
    fun getCategory(id: Long): Flow<Category?>

    /**
     * Retrieves a category by its title.
     *
     * @param title The exact title of the category.
     * @return A [Flow] emitting the category, or null if not found.
     */
    fun getCategory(title: String): Flow<Category?>

    /**
     * Searches for categories that match a query string.
     *
     * @param query The search term (can be empty to return all).
     * @return A [Flow] emitting a list of matching categories.
     */
    fun find(query: String): Flow<List<Category>>
}

