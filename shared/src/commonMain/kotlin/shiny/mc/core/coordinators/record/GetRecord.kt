package shiny.mc.core.coordinators.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.aggregate.Record

interface GetRecord {
    fun getRecord(recordId: String): Flow<Record?>
}