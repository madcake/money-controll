package shiny.mc.core.domain.entity

import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.ValueState

interface RecordInfo {
    val id: String
    val categoryId: Long
    val categoryType: CategoryType
    val title: String
    val estimateValue: String
    val realValue: String
    val valueState: ValueState
    val icon: Any
}