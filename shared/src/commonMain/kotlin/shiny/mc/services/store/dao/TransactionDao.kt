package shiny.mc.services.store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shiny.mc.services.store.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(tx: TransactionEntity): Long

    @Query("DELETE FROM record_transaction WHERE id = :transactionId")
    suspend fun delete(transactionId: Long)

    @Query("""
        SELECT * FROM record_transaction WHERE recordId = :recordId
    """)
    fun getTransactions(recordId: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT
            purpose
        FROM
            record_transaction
        JOIN record ON record_transaction.recordId = record.id AND record.categoryId = :categoryId
        WHERE purpose LIKE '%' || :query || '%'
    """)
    fun getSuggestions(categoryId: Long, query: String): Flow<List<String>>
}