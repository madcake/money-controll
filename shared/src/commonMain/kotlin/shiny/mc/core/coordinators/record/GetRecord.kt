package shiny.mc.core.coordinators.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.aggregate.CategoryRecord

interface GetRecord {
    fun getRecord(recordId: String): Flow<CategoryRecord?>
}