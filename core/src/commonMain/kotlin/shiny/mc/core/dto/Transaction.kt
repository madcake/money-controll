package shiny.mc.core.dto

/**
 * Represents an individual financial transaction.
 *
 * @property id The unique identifier of the transaction, or null if it's new.
 * @property purpose A description of the transaction.
 * @property value The monetary amount of the transaction.
 * @property datetime The timestamp of the transaction in milliseconds.
 */
class Transaction(
    val id: Long? = null,
    val purpose: String,
    val value: Double,
    val datetime: Long,
)