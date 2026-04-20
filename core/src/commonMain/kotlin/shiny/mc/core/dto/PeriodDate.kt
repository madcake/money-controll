package shiny.mc.core.dto

/**
 * Represents a specific month and year in the calendar.
 *
 * @property month The month of the period (1-12).
 * @property year The year of the period.
 */
data class PeriodDate(
    val month: Int,
    val year: Int
) {
    companion object
}