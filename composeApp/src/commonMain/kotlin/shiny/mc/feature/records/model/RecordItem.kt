package shiny.mc.feature.records.model

interface RecordItem {
    val id: Int
    val title: String
    val scheduledValue: String
    val realValue: String
    val valueState: ValueState
}