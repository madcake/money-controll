package shiny.mc.feature.record.ports

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.domain.entity.RecordInfo
import shiny.mc.core.ports.record.GetPeriodRecords
import shiny.mc.feature.record.domain.RecordInfoImpl

@OptIn(ExperimentalCoroutinesApi::class)
class GetPeriodRecordsImpl(
    private val recordRepository: RecordRepository,
) : GetPeriodRecords {

    override fun getRecords(month: Int, year: Int): Flow<List<RecordInfo>> {
        return recordRepository.getRecords(month, year).mapLatest { items ->
            items.map { RecordInfoImpl(it, "RUB") }
        }
    }
}