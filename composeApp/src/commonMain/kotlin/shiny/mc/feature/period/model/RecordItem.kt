package shiny.mc.feature.period.model

interface RecordItem {
    val id: String
    val title: String
    val scheduledValue: String
    val realValue: String
    val valueState: ValueState
}