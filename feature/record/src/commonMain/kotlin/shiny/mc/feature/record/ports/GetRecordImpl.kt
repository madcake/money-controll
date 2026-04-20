package shiny.mc.feature.record.ports

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.domain.entity.RecordInfo
import shiny.mc.core.ports.record.GetRecord
import shiny.mc.feature.record.domain.RecordInfoImpl

class GetRecordImpl(
    private val recordRepository: RecordRepository,
) : GetRecord {

    override fun getRecord(recordId: String): Flow<RecordInfo?> {
        return recordRepository.getRecord(recordId)
            .map {record -> record?.let { RecordInfoImpl(it, "RUB") } }
    }
}