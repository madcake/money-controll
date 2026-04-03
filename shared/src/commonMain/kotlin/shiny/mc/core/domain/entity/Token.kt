package shiny.mc.core.domain.entity

import shiny.mc.core.domain.value.ItemType

class Token (
    val id: Int,
    val categoryRecordId: Int,
    val title: String,
    val count: Float,
    val pricePerItem: Float,
    val itemType: ItemType,
)