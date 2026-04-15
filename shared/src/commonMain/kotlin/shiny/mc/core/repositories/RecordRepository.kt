package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.domain.value.PeriodDate

interface RecordRepository {
    /**
     * Add record for category
     *
     * @param record Category record object
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    suspend fun addRecord(record: Record)

    /**
     * Add records for category
     *
     * @param records Category record object
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    suspend fun addRecords(records: List<Record>)

    /**
     * Get all records in store
     *
     * @return records
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(): Flow<List<Record>>

    /**
     * Get record by record id
     *
     * @param recordId record id for search
     * @return category record or null
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecord(recordId: String): Flow<Record?>

    /**
     * Get records by category id
     *
     * @param categoryId category id for search
     * @return category record or empty list
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(categoryId: Long): Flow<List<Record>>

    /**
     * Get records by period. Period equals month.
     *
     * @param month month number start from 1 (Junuary)
     * @param year year number
     * @return category record or empty list
     * @throws shiny.mc.core.domain.value.CategoryError
     */
    fun getRecords(month: Int, year: Int): Flow<List<Record>>

    /**
     * Update record
     *
     * @param record record
     * @throws shiny.mc.core.domain.value.CategoryError.CategoryNotFound
     */
    suspend fun updateRecord(record: Record)

    /**
     * Check record id
     *
     * @param recordId recordId
     * @throws shiny.mc.core.domain.value.RecordError
     */
    suspend fun hasRecord(recordId: String): Boolean


    /**
     * Remove record
     *
     * @param recordId recordId
     * @throws shiny.mc.core.domain.value.RecordError
     */
    suspend fun removeRecord(recordId: String)

    /**
     * Copy period records and configs to new period
     *
     * @param from copy from period
     * @param to distance to copy
     * @throws shiny.mc.core.domain.value.RecordError
     */
    suspend fun copyPeriod(from: PeriodDate, to: PeriodDate)
}