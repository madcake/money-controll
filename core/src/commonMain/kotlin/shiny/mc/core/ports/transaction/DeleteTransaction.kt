package shiny.mc.core.ports.transaction

/**
 * Port for deleting specific transactions.
 */
interface DeleteTransaction {
    /**
     * Deletes the transaction with the specified ID.
     *
     * @param transactionId The unique identifier of the transaction to delete.
     */
    suspend fun delete(transactionId: Long)
}