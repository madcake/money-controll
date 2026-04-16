package shiny.mc.core.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Record

interface RecordRepository {
    /**
     * Add record for category
     *
     * @param record Category record object
     * @throws shiny.mc.core.dto.error.RecordError
     */
    suspend fun addRecord(record: Record)

    /**
     * Add records for category
     *
     * @param records Category record object
     * @throws shiny.mc.core.dto.error.RecordError
     */
    suspend fun addRecords(records: List<Record>)

    /**
     * Get all records in store
     *
     * @return records
     * @throws shiny.mc.core.dto.error.RecordError
     */
    fun getRecords(): Flow<List<Record>>

    /**
     * Get record by record id
     *
     * @param recordId record id for search
     * @return category record or null
     * @throws shiny.mc.core.dto.error.RecordError
     */
    fun getRecord(recordId: String): Flow<Record?>

    /**
     * Get records by category id
     *
     * @param categoryId category id for search
     * @return category record or empty list
     * @throws shiny.mc.core.dto.error.RecordError
     */
    fun getRecords(categoryId: Long): Flow<List<Record>>

    /**
     * Get records by period. Period equals' month.
     *
     * @param month month number start from 1 (Junuary)
     * @param year year number
     * @return category record or empty list
     * @throws shiny.mc.core.dto.error.RecordError
     */
    fun getRecords(month: Int, year: Int): Flow<List<Record>>

    /**
     * Update record
     *
     * @param record record
     * @throws shiny.mc.core.dto.error.RecordError
     */
    suspend fun updateRecord(record: Record)

    /**
     * Check record id
     *
     * @param recordId recordId
     * @throws shiny.mc.core.dto.error.RecordError
     */
    suspend fun hasRecord(recordId: String): Boolean


    /**
     * Remove record
     *
     * @param recordId recordId
     * @throws shiny.mc.core.dto.error.RecordError
     */
    suspend fun removeRecord(recordId: String)
}