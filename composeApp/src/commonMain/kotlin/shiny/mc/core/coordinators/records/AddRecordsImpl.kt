package shiny.mc.core.coordinators.records

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import shiny.mc.core.coordinators.record.AddRecords
import shiny.mc.core.domain.aggregate.CategoryRecord
import shiny.mc.core.domain.entity.Category
import shiny.mc.core.repositories.CategoryRepository

class AddRecordsImpl(
    private val categoryRepository: CategoryRepository,
) : AddRecords {

    override suspend fun addRecords(
        categories: List<Category>,
        month: Int,
        year: Int
    ) = withContext(Dispatchers.IO) {

        categoryRepository.addRecords(
            records = categories.map {
                val recordId = "${it.id}:$month:$year"
                CategoryRecord(
                    id = recordId,
                    category = it,
                    month = month,
                    year = year,
                    scheduledValue = 0.0,
                )
            },
        )
    }
}