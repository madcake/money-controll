package shiny.mc.core.ports.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Record

/**
 * Port for retrieving a specific transaction record.
 */
interface GetRecord {
    /**
     * Retrieves a record by its unique identifier.
     *
     * @param recordId The unique identifier of the record.
     * @return A [Flow] emitting the [Record] if found, or null otherwise.
     */
    fun getRecord(recordId: String): Flow<Record?>
}