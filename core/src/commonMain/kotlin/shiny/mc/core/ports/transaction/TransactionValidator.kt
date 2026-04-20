package shiny.mc.core.ports.transaction

/**
 * Port for validating transaction data before saving.
 */
interface TransactionValidator {
    /**
     * Validates transaction parameters.
     *
     * @param recordId The ID of the record the transaction is being added to.
     * @param value The transaction amount as a string.
     * @param purpose The transaction purpose/description.
     * @param date The transaction date as a timestamp string.
     * @return True if all parameters are valid.
     * @throws shiny.mc.core.dto.error.TransactionError if any parameter is invalid.
     */
    suspend fun validate(
        recordId: String,
        value: String?,
        purpose: String?,
        date: String?,
    ): Boolean
}