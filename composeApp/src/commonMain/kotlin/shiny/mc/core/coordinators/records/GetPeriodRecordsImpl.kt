package shiny.mc.core.coordinators.records

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shiny.mc.core.coordinators.record.GetPeriodRecords
import shiny.mc.core.domain.aggregate.CategoryRecord
import shiny.mc.core.repositories.CategoryRepository

class GetPeriodRecordsImpl(
    private val categoryRepository: CategoryRepository
) : GetPeriodRecords {
    override fun getRecords(month: Int, year: Int): Flow<List<CategoryRecord>> {
        return categoryRepository.getRecords(month, year)
    }
}