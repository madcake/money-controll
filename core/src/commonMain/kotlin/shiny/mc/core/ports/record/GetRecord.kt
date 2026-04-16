package shiny.mc.core.ports.record

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Record

interface GetRecord {
    fun getRecord(recordId: String): Flow<Record?>
}