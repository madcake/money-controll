package shiny.mc.core.dto

/**
 * Represents a category for transactions (e.g., "Food", "Salary").
 *
 * @property id The unique identifier of the category.
 * @property title The display name of the category.
 * @property type The type of the category (Income or Expense).
 */
data class Category(
    val id: Long = 0,
    val title: String,
    val type: CategoryType,
)