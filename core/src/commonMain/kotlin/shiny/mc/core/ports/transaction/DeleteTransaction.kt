package shiny.mc.core.ports.transaction

interface DeleteTransaction {
    suspend fun delete(transactionId: Long)
}