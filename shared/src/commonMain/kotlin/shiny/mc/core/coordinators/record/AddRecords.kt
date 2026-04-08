package shiny.mc.core.coordinators.record

import shiny.mc.core.domain.entity.Category

interface AddRecords {

    suspend fun addRecords(category: Category, month: Int, year: Int) {
        addRecords(category, month, year)
    }

    suspend fun addRecords(categories: List<Category>, month: Int, year: Int)
}