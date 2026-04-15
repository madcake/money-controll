package shiny.mc.core.coordinators.records

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.repositories.CategoryRepository
import shiny.mc.core.repositories.RecordRepository

class GetRecordImpl(
    private val recordRepository: RecordRepository,
) : GetRecord  {
    override fun getRecord(recordId: String): Flow<Record?> {
        return recordRepository.getRecord(recordId)
    }
}