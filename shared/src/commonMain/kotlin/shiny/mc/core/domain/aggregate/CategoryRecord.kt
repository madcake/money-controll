package shiny.mc.core.domain.aggregate

import shiny.mc.core.domain.entity.Category

class CategoryRecord(
    val id: String,
    val category: Category,
    val month: Int,
    val year: Int,
    val scheduledValue: Double,
)