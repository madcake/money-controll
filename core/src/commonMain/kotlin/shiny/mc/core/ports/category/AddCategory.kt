package shiny.mc.core.ports.category

import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.error.CategoryError

/**
 * Port for adding new transaction categories.
 */
interface AddCategory {

    /**
     * Adds a new category with the specified title and type.
     *
     * @param title The display name of the category.
     * @param type The type of the category (e.g., Income or Expense).
     * @return True if the category was successfully added, false otherwise.
     * @throws CategoryError.EmptyTitle if the title is empty.
     * @throws CategoryError.DuplicatedTitle if a category with the same title already exists.
     */
    suspend fun addCategory(title: String, type: CategoryType): Boolean
}