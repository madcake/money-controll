package shiny.mc.infrastructure.persistent.room.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.adapters.TransactionRepository
import shiny.mc.core.dto.Transaction
import shiny.mc.infrastructure.persistent.room.dao.TransactionDao
import shiny.mc.infrastructure.persistent.room.entity.toDto
import shiny.mc.infrastructure.persistent.room.entity.toEntity

class RoomTransactionRepository(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getRecordTransactions(recordId: String): Flow<List<Transaction>> {
        return transactionDao.getTransactions(recordId).toDto()
    }

    override suspend fun addRecordTransaction(
        recordId: String,
        transaction: Transaction
    ) {
        transactionDao.insert(transaction.toEntity(recordId))
    }

    override suspend fun deleteTransaction(transactionId: Long) {
        transactionDao.delete(transactionId)
    }

    override fun getSuggestions(
        categoryId: Long,
        query: String
    ): Flow<List<String>> {
        return transactionDao.getSuggestions(categoryId, query)
    }
}