package shiny.mc.infrastructure.persistent.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import shiny.mc.infrastructure.persistent.room.entity.RecordEntity
import shiny.mc.infrastructure.persistent.room.entity.RecordSummaryEntity

@Dao
interface RecordDao {

    @Insert
    suspend fun insert(record: RecordEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(records: List<RecordEntity>)

    @Update
    suspend fun update(entity: RecordEntity)

    @Query("DELETE FROM record WHERE id = :recordId")
    suspend fun delete(recordId: String)

    @Query(
        """
        SELECT
            record.id,
            record.categoryId,
            record.month,
            record.year,
            record.estimateValue,
            category.title,
            category.type,
            SUM(record_transaction.value) as real
        FROM
            record
        JOIN category ON record.categoryId = category.id
        LEFT JOIN record_transaction ON record.id = record_transaction.recordId
        GROUP BY record.id
    """
    )
    fun getRecords(): Flow<List<RecordSummaryEntity>>

    @Query(
        """
        SELECT
            record.id,
            record.categoryId,
            record.month,
            record.year,
            record.estimateValue,
            category.title,
            category.type,
            SUM(record_transaction.value) as real
        FROM
            record
        JOIN category ON record.categoryId = category.id
        LEFT JOIN record_transaction ON record.id = record_transaction.recordId
        WHERE categoryId = :categoryId
        GROUP BY record.id
    """
    )
    fun getRecords(categoryId: Long): Flow<List<RecordSummaryEntity>>

    @Query(
        """
        SELECT
            record.id,
            record.categoryId,
            record.month,
            record.year,
            record.estimateValue,
            category.title,
            category.type,
            SUM(record_transaction.value) as real
        FROM
            record
        JOIN category ON record.categoryId = category.id
        LEFT JOIN record_transaction ON record.id = record_transaction.recordId
        WHERE
            month = :month AND year = :year
        GROUP BY record.id
    """
    )
    fun getRecords(month: Int, year: Int): Flow<List<RecordSummaryEntity>>

    @Query(
        """
        SELECT
            record.id,
            record.categoryId,
            record.month,
            record.year,
            record.estimateValue,
            category.title,
            category.type,
            SUM(record_transaction.value) as real
        FROM
            record
        JOIN category ON record.categoryId = category.id
        LEFT JOIN record_transaction ON record.id = record_transaction.recordId
        WHERE
            record.id = :recordId
        GROUP BY record.id
    """
    )
    fun getRecord(recordId: String): Flow<RecordSummaryEntity?>

    @Query("""
        SELECT EXISTS(SELECT id FROM record WHERE id = :recordId)
    """)
    suspend fun hasRecord(recordId: String): Boolean
}