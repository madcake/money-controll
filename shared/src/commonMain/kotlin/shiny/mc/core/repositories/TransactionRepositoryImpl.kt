package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.Transaction
import shiny.mc.services.store.dao.TransactionDao
import shiny.mc.services.store.entity.toDto
import shiny.mc.services.store.entity.toEntity

class TransactionRepositoryImpl(
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
}