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

    @Query("""
        SELECT * FROM record_transaction WHERE recordId = :recordId
    """)
    fun getTransactions(recordId: String): Flow<List<TransactionEntity>>
}