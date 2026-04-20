package shiny.mc.core.ports.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.RecordInfo

/**
 * Port for retrieving all records for a specific period.
 */
interface GetPeriodRecords {
    /**
     * Returns a list of records for the specified month and year.
     *
     * @param month The month (1-12).
     * @param year The year.
     * @return A [Flow] emitting a list of [RecordInfo] for the period.
     */
    fun getRecords(month: Int, year: Int): Flow<List<RecordInfo>>
}