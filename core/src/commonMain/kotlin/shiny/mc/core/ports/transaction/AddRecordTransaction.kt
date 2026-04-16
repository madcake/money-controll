package shiny.mc.core.ports.transaction

interface AddRecordTransaction {

    /**
     * @throws shiny.mc.core.dto.error.TransactionError
     */
    suspend fun addTransaction(
        recordId: String,
        value: String,
        purpose: String,
        datetime: Long,
    )
}