package shiny.mc.core.coordinators.records

import kotlinx.coroutines.flow.firstOrNull
import shiny.mc.core.coordinators.record.UpdateRecordValue
import shiny.mc.core.repositories.RecordRepository
import shiny.mc.platform.parseToDoubleOrNull

class UpdateRecordValueImpl(
    private val recordRepository: RecordRepository,
) : UpdateRecordValue {

    override suspend fun update(recordId: String, estimateValue: String) {
        val estimateValue = estimateValue.parseToDoubleOrNull() ?: return
        val record = recordRepository.getRecord(recordId).firstOrNull()
            ?.copy(estimateValue = estimateValue)
            ?: return

        recordRepository.updateRecord(record)
    }
}