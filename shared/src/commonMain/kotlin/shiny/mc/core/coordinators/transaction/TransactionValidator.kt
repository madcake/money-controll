package shiny.mc.core.coordinators.transaction

interface TransactionValidator {
    /**
     * Validate transaction data
     *
     * @param value transaction value
     * @param purpose transaction purpose
     * @return true for valid parameters
     * @throws shiny.mc.core.domain.value.TransactionError
     */
    suspend fun validate(
        recordId: String,
        value: String?,
        purpose: String?,
        date: String?,
    ): Boolean
}