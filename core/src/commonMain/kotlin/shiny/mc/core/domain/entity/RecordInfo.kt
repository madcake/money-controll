package shiny.mc.core.domain.entity

import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.ValueState

interface RecordInfo {
    val id: String
    val categoryId: Long
    val categoryType: CategoryType
    val title: String
    val estimateValue: Double
    val estimateValueFormatted: String
    val realValue: String
    val valueState: ValueState
    val period: PeriodDate
    val icon: Any
}