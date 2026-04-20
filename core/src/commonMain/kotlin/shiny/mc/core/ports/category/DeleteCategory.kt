package shiny.mc.core.ports.category

/**
 * Port for deleting transaction categories.
 */
interface DeleteCategory {
    /**
     * Deletes the category with the specified ID.
     *
     * @param categoryId The unique identifier of the category to delete.
     */
    suspend fun deleteCategory(categoryId: Long)
}