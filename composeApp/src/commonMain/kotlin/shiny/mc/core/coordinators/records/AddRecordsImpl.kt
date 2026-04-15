package shiny.mc.core.coordinators.records

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import shiny.mc.core.coordinators.record.AddRecords
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.domain.aggregate.createId
import shiny.mc.core.domain.entity.Category
import shiny.mc.core.domain.value.RecordError
import shiny.mc.core.repositories.RecordRepository

class AddRecordsImpl(
    private val recordRepository: RecordRepository,
) : AddRecords {

    override suspend fun addRecords(
        categories: List<Category>,
        month: Int,
        year: Int
    ) = withContext(Dispatchers.IO) {

        recordRepository.addRecords(
            records = categories.map {
                val categoryId = it.id ?: throw RecordError.CategoryNoneExist()
                val recordId = Record.createId(categoryId, month, year)
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