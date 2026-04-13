package shiny.mc.core.coordinators.transaction

import shiny.mc.core.repositories.TransactionRepository

class DeleteTransactionImpl(
    private val transactionRepository: TransactionRepository,
) : DeleteTransaction {
    override suspend fun delete(transactionId: Long) {
        transactionRepository.deleteTransaction(transactionId)
    }
}