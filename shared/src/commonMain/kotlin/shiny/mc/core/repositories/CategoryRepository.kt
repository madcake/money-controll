package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.aggregate.Record
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
    suspend fun addRecord(record: Record)

    /**
     * Add records for category
     *
     * @param records Category record object
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    suspend fun addRecords(records: List<Record>)

    /**
     * Get all records in store
     *
     * @return records
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(): Flow<List<Record>>

    /**
     * Get record by record id
     *
     * @param recordId record id for search
     * @return category record or null
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecord(recordId: String): Flow<Record?>

    /**
     * Get records by category id
     *
     * @param categoryId category id for search
     * @return category record or empty list
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(categoryId: Long): Flow<List<Record>>

    /**
     * Get records by period. Period equals month.
     *
     * @param month month number start from 1 (Junuary)
     * @param year year number
     * @return category record or empty list
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(month: Int, year: Int): Flow<List<Record>>

    /**
     * Update record
     *
     * @param record record
     * @throws shiny.mc.core.domain.value.CategoryError.CategoryNotFound
     */
    suspend fun updateRecord(record: Record)
}

