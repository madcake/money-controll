package shiny.mc.services.store.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shiny.mc.services.store.entity.PeriodViewEntity

@Dao
interface PeriodDao {

    @Query(
        """
        SELECT
            records.month,
            records.year,
            SUM(records.inEstimateValue) as inEstimate,
            SUM(records.outEstimateValue) as outEstimate,
            SUM(records.inRealValue) as inReal,
            SUM(records.outRealValue) as outReal
        FROM
            (
                SELECT
                    record.id,
                    record."month",
                    record."year",
                    category.type,
                    IIF(category.type = 'Asset', record.scheduledValue, 0) AS inEstimateValue,
                    IIF(category.type = 'Liability', record.scheduledValue, 0) AS outEstimateValue,
                    SUM(IIF(category.type = 'Asset', record_transaction."value", 0)) AS inRealValue,
                    SUM(IIF(category.type = 'Liability', record_transaction."value", 0)) AS outRealValue
                FROM
                    record
                JOIN category ON record.categoryId = category.id
                LEFT JOIN record_transaction ON record.id = record_transaction.recordId
                GROUP BY record.id
            ) AS records
        GROUP BY records.month, records.year;
    """
    )
    fun getPeriods(): Flow<List<PeriodViewEntity>>

    @Query(
        """
        SELECT
            records.month,
            records.year,
            SUM(records.inEstimateValue) as inEstimate,
            SUM(records.outEstimateValue) as outEstimate,
            SUM(records.inRealValue) as inReal,
            SUM(records.outRealValue) as outReal
        FROM
            (
                SELECT
                    record.id,
                    record."month",
                    record."year",
                    category.type,
                    IIF(category.type = 'Asset', record.scheduledValue, 0) AS inEstimateValue,
                    IIF(category.type = 'Liability', record.scheduledValue, 0) AS outEstimateValue,
                    SUM(IIF(category.type = 'Asset', record_transaction."value", 0)) AS inRealValue,
                    SUM(IIF(category.type = 'Liability', record_transaction."value", 0)) AS outRealValue
                FROM
                    record
                JOIN category ON record.categoryId = category.id
                LEFT JOIN record_transaction ON record.id = record_transaction.recordId
                GROUP BY record.id
            ) AS records
        WHERE records.month = :month AND records.year = :year
        GROUP BY records.month, records.year;
    """
    )
    fun getPeriod(month: Int, year: Int): Flow<PeriodViewEntity?>

    @Query("""
        INSERT INTO record SELECT
            categoryId || ':' || :toMonth || ':' || :toYear AS recordId,
            categoryId,
            :toMonth AS month,
            :toYear AS year,
            scheduledValue
        FROM record WHERE month = :fromMonth AND year = :fromYear;
    """)
    suspend fun copy(fromMonth: Int, fromYear: Int, toMonth: Int, toYear: Int)
}