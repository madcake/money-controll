package shiny.mc.core.coordinators.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.aggregate.CategoryRecord

interface GetPeriodRecords {
    fun getRecords(month: Int, year: Int): Flow<List<CategoryRecord>>
}