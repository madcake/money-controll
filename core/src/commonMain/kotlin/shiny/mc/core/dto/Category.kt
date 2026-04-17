package shiny.mc.core.dto

data class Category(
    val id: Long = 0,
    val title: String,
    val type: CategoryType,
)