package shiny.mc.core.dto

class Transaction(
    val id: Long? = null,
    val purpose: String,
    val value: Double,
    val datetime: Long,
)