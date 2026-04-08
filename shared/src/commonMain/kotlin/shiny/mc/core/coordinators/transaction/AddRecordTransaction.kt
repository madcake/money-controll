package shiny.mc.core.coordinators.transaction

interface AddRecordTransaction {

    /**
     * @throws shiny.mc.core.domain.value.TransactionError
     */
    suspend fun addTransaction(
        recordId: String,
        value: String,
        purpose: String,
        datetime: Long,
    )
}