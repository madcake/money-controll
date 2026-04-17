package shiny.mc.core.ports.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.RecordInfo

interface GetPeriodRecords {
    fun getRecords(month: Int, year: Int): Flow<List<RecordInfo>>
}