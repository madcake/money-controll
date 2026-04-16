package shiny.mc.core.coordinators.transaction

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.adapters.TransactionRepository
import shiny.mc.core.dto.Transaction
import shiny.mc.core.ports.transaction.GetRecordTransactions

class GetRecordTransactionsImpl(
    private val transactionRepository: TransactionRepository
) : GetRecordTransactions {
    override fun transactions(recordId: String): Flow<List<Transaction>> {
        return transactionRepository.getRecordTransactions(recordId)
    }
}