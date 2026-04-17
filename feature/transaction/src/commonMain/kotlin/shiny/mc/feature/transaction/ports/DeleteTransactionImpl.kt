package shiny.mc.feature.transaction.ports

import shiny.mc.core.adapters.TransactionRepository
import shiny.mc.core.ports.transaction.DeleteTransaction

class DeleteTransactionImpl(
    private val transactionRepository: TransactionRepository,
) : DeleteTransaction {
    override suspend fun delete(transactionId: Long) {
        transactionRepository.deleteTransaction(transactionId)
    }
}