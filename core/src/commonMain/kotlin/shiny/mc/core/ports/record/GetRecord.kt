package shiny.mc.core.ports.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.RecordInfo

/**
 * Port for retrieving a specific transaction record.
 */
interface GetRecord {
    /**
     * Retrieves a record by its unique identifier.
     *
     * @param recordId The unique identifier of the record.
     * @return A [Flow] emitting the [RecordInfo] if found, or null otherwise.
     */
    fun getRecord(recordId: String): Flow<RecordInfo?>
}