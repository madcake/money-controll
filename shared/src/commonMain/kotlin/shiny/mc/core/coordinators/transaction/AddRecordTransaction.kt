package shiny.mc.core.coordinators.transaction

interface AddRecordTransaction {
    suspend fun addTransaction(
        recordId: String,
        value: Float,
        purpose: String,
        datetime: Long,
    )
}