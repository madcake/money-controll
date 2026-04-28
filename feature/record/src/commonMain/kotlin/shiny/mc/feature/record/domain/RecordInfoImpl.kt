package shiny.mc.feature.record.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.Stable
import shiny.mc.core.domain.entity.RecordInfo
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.Record
import shiny.mc.core.dto.ValueState
import shiny.mc.platform.format

@Stable
class RecordInfoImpl(
    override val id: String,
    override val categoryId: Long,
    override val categoryType: CategoryType,
    override val title: String,
    override val estimateValue: Double,
    override val estimateValueFormatted: String,
    override val realValue: String,
    override val valueState: ValueState,
    override val icon: Any,
    override val period: PeriodDate,
) : RecordInfo {

    constructor(dto: Record, currencyCode: String) : this(
        id = dto.id,
        categoryId = dto.category.id,
        categoryType = dto.category.type,
        title = dto.category.title,
        estimateValue = dto.estimateValue,
        estimateValueFormatted = dto.estimateValue.format(currencyCode),
        realValue = dto.realValue.format(currencyCode),
        valueState = when (dto.category.type) {
            CategoryType.In -> if (dto.estimateValue <= dto.realValue) {
                ValueState.Surplus
            } else {
                ValueState.Deficit
            }
            CategoryType.Out -> if (dto.estimateValue >= dto.realValue) {
                ValueState.Surplus
            } else {
                ValueState.Deficit
            }
        },
        icon = when (dto.category.type) {
            CategoryType.In -> Icons.Default.ArrowDropDown
            CategoryType.Out -> Icons.Default.ArrowDropUp
        },
        period = PeriodDate(dto.month, dto.year),
    )
}