package shiny.mc.core.domain.entity

class Transaction(
    val id: Int,
    val categoryRecordId: Int,
    val value: Float,
    val datetime: Long? = null,
    val token: Token? = null,
)