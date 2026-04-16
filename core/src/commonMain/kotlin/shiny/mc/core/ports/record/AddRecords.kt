package shiny.mc.core.ports.record

import shiny.mc.core.dto.Category

interface AddRecords {

    suspend fun addRecords(category: Category, month: Int, year: Int) {
        addRecords(category, month, year)
    }

    suspend fun addRecords(categories: List<Category>, month: Int, year: Int)
}