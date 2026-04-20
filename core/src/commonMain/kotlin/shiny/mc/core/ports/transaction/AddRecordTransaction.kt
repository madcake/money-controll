package shiny.mc.core.ports.transaction

/**
 * Port for adding a new transaction to a specific record.
 */
interface AddRecordTransaction {

    /**
     * Adds a new transaction.
     *
     * @param recordId The ID of the record this transaction belongs to.
     * @param value The amount of the transaction as a string.
     * @param purpose The description or purpose of the transaction.
     * @param datetime The timestamp of the transaction in milliseconds.
     * @throws shiny.mc.core.dto.error.TransactionError if validation fails.
     */
    suspend fun addTransaction(
        recordId: String,
        value: String,
        purpose: String,
        datetime: Long,
    )
}