package shiny.mc.core.coordinators.records

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.dto.Category
import shiny.mc.core.dto.Record
import shiny.mc.core.dto.error.RecordError
import shiny.mc.core.ports.record.AddRecords
import shiny.mc.core.ports.record.CreateRecordId

class AddRecordsImpl(
    private val recordRepository: RecordRepository,
    private val createRecordId: CreateRecordId = object : CreateRecordId {}
) : AddRecords {

    override suspend fun addRecords(
        categories: List<Category>,
        month: Int,
        year: Int
    ) = withContext(Dispatchers.IO) {

        recordRepository.addRecords(
            records = categories.map {
                val categoryId = it.id ?: throw RecordError.CategoryNoneExist()
                val recordId = createRecordId.createId(categoryId, month, year)
                Record(
                    id = recordId,
                    category = it,
                    month = month,
                    year = year,
                    estimateValue = 0.0,
                    realValue = 0.0
                )
            },
        )
    }
}