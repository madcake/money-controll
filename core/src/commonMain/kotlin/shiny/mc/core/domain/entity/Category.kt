package shiny.mc.core.domain.entity

import shiny.mc.core.dto.Category
import shiny.mc.core.dto.error.CategoryError

/**
 * Validates the category data.
 *
 * @throws CategoryError.EmptyTitle if the category title is empty.
 */
fun Category.validate() {
    if (title.isEmpty()) throw CategoryError.EmptyTitle()
}