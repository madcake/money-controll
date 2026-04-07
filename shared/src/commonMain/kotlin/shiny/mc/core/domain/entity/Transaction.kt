package shiny.mc.core.domain.entity

class Transaction(
    val id: Long? = null,
    val purpose: String,
    val value: Double,
    val datetime: Long,
)