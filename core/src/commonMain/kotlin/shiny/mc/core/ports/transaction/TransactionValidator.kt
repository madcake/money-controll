package shiny.mc.core.ports.transaction

interface TransactionValidator {
    /**
     * Validate transaction data
     *
     * @param value transaction value
     * @param purpose transaction purpose
     * @return true for valid parameters
     * @throws shiny.mc.core.dto.error.TransactionError
     */
    suspend fun validate(
        recordId: String,
        value: String?,
        purpose: String?,
        date: String?,
    ): Boolean
}