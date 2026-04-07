package shiny.mc.core.coordinators.records

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import shiny.mc.core.coordinators.record.UpdateRecord
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.repositories.CategoryRepository

class UpdateRecordImpl(
    private val categoryRepository: CategoryRepository,
) : UpdateRecord {
    override suspend fun update(record: Record) = withContext(Dispatchers.IO) {
        categoryRepository.updateRecord(record)
    }
}