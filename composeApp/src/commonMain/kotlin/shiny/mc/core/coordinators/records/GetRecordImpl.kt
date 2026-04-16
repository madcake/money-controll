package shiny.mc.core.coordinators.records

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.dto.Record
import shiny.mc.core.ports.record.GetRecord

class GetRecordImpl(
    private val recordRepository: RecordRepository,
) : GetRecord {
    override fun getRecord(recordId: String): Flow<Record?> {
        return recordRepository.getRecord(recordId)
    }
}