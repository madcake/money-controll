package shiny.mc.core.domain.aggregate

import shiny.mc.core.domain.entity.Category

data class Record(
    val id: String,
    val category: Category, // TODO: Remove it
    val month: Int,
    val year: Int,
    val estimateValue: Double,
    val realValue: Double,
) {
    companion object
}

fun Record.Companion.createId(categoryId: Long, month: Int, year: Int) = "${categoryId}:$month:$year"