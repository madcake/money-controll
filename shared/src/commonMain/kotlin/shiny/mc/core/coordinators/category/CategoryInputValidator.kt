package shiny.mc.core.coordinators.category

import shiny.mc.core.domain.value.CategoryType

interface CategoryInputValidator {
    /**
     * Validate user input for new category
     *
     * @param title category title
     * @param type category type
     * @return true if data valid otherwise false or throw exception
     * @throws shiny.mc.core.domain.value.CategoryError if data is invalid
     */
    suspend fun validateCategoryInput(title: String?, type: CategoryType?): Boolean
}