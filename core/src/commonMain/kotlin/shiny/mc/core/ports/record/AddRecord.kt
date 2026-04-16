package shiny.mc.core.ports.record

import shiny.mc.core.dto.Category

interface AddRecord {
    fun addRecord(
        category: Category,
        start: Long,
        scheduledValue: Float,
    )
}