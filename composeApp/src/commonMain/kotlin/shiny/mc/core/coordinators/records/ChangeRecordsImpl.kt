package shiny.mc.core.coordinators.records

import kotlinx.coroutines.flow.firstOrNull
import shiny.mc.core.coordinators.record.ChangeRecords
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.domain.aggregate.createId
import shiny.mc.core.domain.entity.Category
import shiny.mc.core.domain.value.RecordError
import shiny.mc.core.repositories.CategoryRepository

class ChangeRecordsImpl(
    private val categoryRepository: CategoryRepository,
) : ChangeRecords {
    override suspend fun changeRecord(
        category: Category,
        month: Int,
        year: Int
    ) {
        val categoryId = category.id ?: throw RecordError.CategoryNoneExist()

        val recordId = Record.createId(categoryId, month, year)

        if (categoryRepository.hasRecord(recordId)) {
            categoryRepository.removeRecord(recordId)
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
            categoryRepository.addRecord(record)
        }
    }
}