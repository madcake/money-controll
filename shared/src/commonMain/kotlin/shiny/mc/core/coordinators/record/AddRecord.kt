package shiny.mc.core.coordinators.record

import shiny.mc.core.domain.entity.Category

interface AddRecord {
    fun addRecord(
        category: Category,
        start: Long,
        scheduledValue: Float,
    )
}