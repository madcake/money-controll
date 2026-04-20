package shiny.mc.core.domain.value

data class TransactionValue(
    val recordId: String,
    val purpose: String,
    val value: String,
    val date: Long,
)