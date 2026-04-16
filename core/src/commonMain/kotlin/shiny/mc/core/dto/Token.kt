package shiny.mc.core.dto

class Token (
    val id: Int,
    val categoryRecordId: Int,
    val title: String,
    val count: Float,
    val pricePerItem: Float,
    val itemType: ItemType,
)