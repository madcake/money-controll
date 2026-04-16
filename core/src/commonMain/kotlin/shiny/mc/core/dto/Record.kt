package shiny.mc.core.dto

data class Record(
    val id: String,
    val category: Category,
    val month: Int,
    val year: Int,
    val estimateValue: Double,
    val realValue: Double,
)