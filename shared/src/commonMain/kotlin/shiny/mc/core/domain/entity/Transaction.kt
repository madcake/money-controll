package shiny.mc.core.domain.entity

class Transaction(
    val id: Long? = null,
    val purpose: String,
    val value: Float,
    val datetime: Long,
)