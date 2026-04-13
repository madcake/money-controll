package shiny.mc.feature.period.model

import shiny.mc.core.domain.value.ValueState

interface RecordItem {
    val id: String
    val title: String
    val scheduledValue: String
    val realValue: String
    val valueState: ValueState
}