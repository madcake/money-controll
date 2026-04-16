package shiny.mc.core.coordinators.records

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.dto.Record
import shiny.mc.core.ports.record.GetPeriodRecords

class GetPeriodRecordsImpl(
    private val recordRepository: RecordRepository,
) : GetPeriodRecords {
    override fun getRecords(month: Int, year: Int): Flow<List<Record>> {
        return recordRepository.getRecords(month, year)
    }
}