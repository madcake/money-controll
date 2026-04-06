package shiny.mc.core.coordinators.transaction

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.Transaction
import shiny.mc.core.repositories.TransactionRepository

class GetRecordTransactionsImpl(
    private val transactionRepository: TransactionRepository
) : GetRecordTransactions {
    override fun transactions(recordId: String): Flow<List<Transaction>> {
        return transactionRepository.getRecordTransactions(recordId)
    }
}