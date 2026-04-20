package shiny.mc.core.ports.record

import shiny.mc.core.dto.Category

/**
 * Port for adding transaction records for specific categories and periods.
 */
interface AddRecords {

    /**
     * Adds a record for a single category in a specific month and year.
     *
     * @param category The category to add a record for.
     * @param month The month (1-12).
     * @param year The year.
     */
    suspend fun addRecords(category: Category, month: Int, year: Int) {
        addRecords(listOf(category), month, year)
    }

    /**
     * Adds records for multiple categories in a specific month and year.
     *
     * @param categories The list of categories to add records for.
     * @param month The month (1-12).
     * @param year The year.
     */
    suspend fun addRecords(categories: List<Category>, month: Int, year: Int)
}