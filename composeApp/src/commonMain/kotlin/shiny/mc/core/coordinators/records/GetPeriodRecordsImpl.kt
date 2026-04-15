package shiny.mc.core.coordinators.records

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.coordinators.record.GetPeriodRecords
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.repositories.RecordRepository

class GetPeriodRecordsImpl(
    private val recordRepository: RecordRepository,
) : GetPeriodRecords {
    override fun getRecords(month: Int, year: Int): Flow<List<Record>> {
        return recordRepository.getRecords(month, year)
    }
}