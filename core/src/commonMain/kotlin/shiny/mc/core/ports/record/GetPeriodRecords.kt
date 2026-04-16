package shiny.mc.core.ports.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Record

interface GetPeriodRecords {
    fun getRecords(month: Int, year: Int): Flow<List<Record>>
}