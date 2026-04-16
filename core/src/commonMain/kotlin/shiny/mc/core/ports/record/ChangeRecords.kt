package shiny.mc.core.ports.record

import shiny.mc.core.dto.Category

interface ChangeRecords {
    suspend fun changeRecord(category: Category, month: Int, year: Int)
}