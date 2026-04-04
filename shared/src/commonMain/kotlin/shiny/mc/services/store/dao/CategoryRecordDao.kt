package shiny.mc.services.store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import shiny.mc.services.store.entity.RecordCategoryEntity
import shiny.mc.services.store.entity.RecordEntity

@Dao
interface CategoryRecordDao {

    @Insert
    suspend fun insert(record: RecordEntity)

    @Insert
    suspend fun insert(records: List<RecordEntity>)

    @Transaction
    @Query("""
        SELECT
            record.id,
            record.categoryId,
            record.month,
            record.year,
            record.scheduledValue,
            category.title,
            category.type
        FROM
            record
        JOIN category ON record.categoryId = category.id
        WHERE
            month = :month AND year = :year
    """)
    fun getRecords(month: Int, year: Int): Flow<List<RecordCategoryEntity>>

}