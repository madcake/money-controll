package shiny.mc.core.coordinators.transaction

interface DeleteTransaction {
    suspend fun delete(transactionId: Long)
}