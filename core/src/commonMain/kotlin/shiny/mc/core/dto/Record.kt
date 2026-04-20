package shiny.mc.core.dto

/**
 * Represents a financial record for a specific category in a given month and year.
 *
 * @property id The unique identifier for this record.
 * @property category The category associated with this record.
 * @property month The month (1-12) for this record.
 * @property year The year for this record.
 * @property estimateValue The planned or estimated budget amount.
 * @property realValue The actual spent or received amount.
 */
data class Record(
    val id: String,
    val category: Category,
    val month: Int,
    val year: Int,
    val estimateValue: Double,
    val realValue: Double,
)