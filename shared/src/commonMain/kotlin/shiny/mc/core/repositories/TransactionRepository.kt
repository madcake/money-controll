package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.Transaction

interface TransactionRepository {

    fun getRecordTransactions(recordId: String): Flow<List<Transaction>>

    suspend fun addRecordTransaction(recordId: String, transaction: Transaction)

    suspend fun deleteTransaction(transactionId: Long)
}