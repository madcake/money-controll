package shiny.mc.feature.transaction.ports

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shiny.mc.core.adapters.TransactionRepository
import shiny.mc.core.domain.entity.TransactionInfo
import shiny.mc.core.ports.transaction.GetRecordTransactions
import shiny.mc.feature.transaction.domain.TransactionInfoImpl

class GetRecordTransactionsImpl(
    private val transactionRepository: TransactionRepository
) : GetRecordTransactions {
    override fun transactions(recordId: String): Flow<List<TransactionInfo>> {
        return transactionRepository.getRecordTransactions(recordId).map { txs ->
            txs.map { TransactionInfoImpl(it) }
        }
    }
}