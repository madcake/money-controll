package shiny.mc.core.coordinators.records

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.repositories.CategoryRepository

class GetRecordImpl(
    private val categoryRepository: CategoryRepository,
) : GetRecord  {
    override fun getRecord(recordId: String): Flow<Record?> {
        return categoryRepository.getRecord(recordId)
    }
}