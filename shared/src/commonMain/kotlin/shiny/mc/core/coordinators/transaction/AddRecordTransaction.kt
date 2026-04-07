package shiny.mc.core.coordinators.transaction

interface AddRecordTransaction {
    suspend fun addTransaction(
        recordId: String,
        value: Double,
        purpose: String,
        datetime: Long,
    )
}