package shiny.mc.core.ports.record

import shiny.mc.core.dto.Category

/**
 * Port for managing record associations with periods.
 */
interface ChangeRecords {
    /**
     * Associates a category with a specific period (month and year).
     *
     * @param categoryId The ID of the category to add to the period.
     * @param month The month (1-12).
     * @param year The year.
     */
    suspend fun addToPeriod(categoryId: Long, month: Int, year: Int)
}