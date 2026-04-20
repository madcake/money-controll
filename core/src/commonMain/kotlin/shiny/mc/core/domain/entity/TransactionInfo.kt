package shiny.mc.core.domain.entity

interface TransactionInfo {
    val id: Long
    val title: String
    val value: String
    val detailedValue: String
    val date: String
}