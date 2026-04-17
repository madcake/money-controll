package shiny.mc.feature.record.ports

import kotlinx.coroutines.flow.firstOrNull
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.dto.Category
import shiny.mc.core.dto.Record
import shiny.mc.core.dto.error.RecordError
import shiny.mc.core.ports.record.ChangeRecords
import shiny.mc.core.ports.record.CreateRecordId

class ChangeRecordsImpl(
    private val recordRepository: RecordRepository,
    private val categoryRepository: CategoryRepository,
    private val createRecordId: CreateRecordId = object : CreateRecordId {}
) : ChangeRecords {
    override suspend fun changeRecord(
        category: Category,
        month: Int,
        year: Int
    ) {
        val categoryId = category.id ?: throw RecordError.CategoryNoneExist()

        val recordId = createRecordId.createId(categoryId, month, year)

        if (recordRepository.hasRecord(recordId)) {
            recordRepository.removeRecord(recordId)
        } else {
            val category = categoryRepository.getCategory(categoryId).firstOrNull() ?: return
            val record = Record(
                id = recordId,
                category = category,
                month = month,
                year = year,
                estimateValue = 0.0,
                realValue = 0.0,
            )
            recordRepository.addRecord(record)
        }
    }
}