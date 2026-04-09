package shiny.mc.services.store.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shiny.mc.services.store.entity.PeriodViewEntity

@Dao
interface PeriodDao {

    @Query("""
        SELECT
            records.month,
            records.year,
            SUM(records.assetScheduledValue) as assetScheduled,
            SUM(records.liabilityScheduledValue) as liabilityScheduled,
            SUM(records.assetRealValue) as assetReal,
            SUM(records.liabilityRealValue) as liabilityReal
        FROM
            (
                SELECT
                    record.id,
                    record."month",
                    record."year",
                    category.type,
                    IIF(category.type = 'Asset', record.scheduledValue, 0) AS assetScheduledValue,
                    IIF(category.type = 'Liability', record.scheduledValue, 0) AS liabilityScheduledValue,
                    SUM(IIF(category.type = 'Asset', record_transaction."value", 0)) AS assetRealValue,
                    SUM(IIF(category.type = 'Liability', record_transaction."value", 0)) AS liabilityRealValue
                FROM
                    record
                JOIN category ON record.categoryId = category.id
                LEFT JOIN record_transaction ON record.id = record_transaction.recordId
                GROUP BY record.id
            ) AS records
        GROUP BY records.month, records.year;
    """)
    fun getPeriods(): Flow<List<PeriodViewEntity>>
}