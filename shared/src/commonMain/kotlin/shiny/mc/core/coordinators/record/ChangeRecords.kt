package shiny.mc.core.coordinators.record

import shiny.mc.core.domain.entity.Category

interface ChangeRecords {
    suspend fun changeRecord(category: Category, month: Int, year: Int)
}