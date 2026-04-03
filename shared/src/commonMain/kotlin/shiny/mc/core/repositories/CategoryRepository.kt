package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.aggregate.CategoryRecord
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

    /**
     * Add record for category
     *
     * @param record Category record object
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    suspend fun addRecord(record: CategoryRecord)

    /**
     * Get all records in store
     *
     * @return records
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(): Flow<List<CategoryRecord>>

    /**
     * Get record by record id
     *
     * @param recordId record id for search
     * @return category record or null
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecord(recordId: String): Flow<CategoryRecord?>

    /**
     * Get records by category id
     *
     * @param categoryId category id for search
     * @return category record or empty list
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(categoryId: Long): Flow<List<CategoryRecord>>

    /**
     * Get records by period
     *
     * @param start period start in unix timestamp ms
     * @param end period end in unix timestamp ms
     * @return category record or empty list
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(start: Long, end: Long): Flow<List<CategoryRecord>>
}

