package shiny.mc.core.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Transaction

interface TransactionRepository {

    fun getRecordTransactions(recordId: String): Flow<List<Transaction>>

    suspend fun addRecordTransaction(recordId: String, transaction: Transaction)

    suspend fun deleteTransaction(transactionId: Long)

    fun getSuggestions(categoryId: Long, query: String): Flow<List<String>>
}