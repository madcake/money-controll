package shiny.mc.core.coordinators.record

import shiny.mc.core.domain.aggregate.Record

interface UpdateRecord {
    suspend fun update(record: Record)
}