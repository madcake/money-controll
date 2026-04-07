package shiny.mc.core.domain.aggregate

import shiny.mc.core.domain.entity.Category

data class Record(
    val id: String,
    val category: Category, // TODO: Remove it
    val month: Int,
    val year: Int,
    val scheduledValue: Double,
    val realValue: Double,
)