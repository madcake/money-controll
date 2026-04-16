package shiny.mc.core.ports.record

interface UpdateRecordValue {
    suspend fun update(recordId: String, estimateValue: String)
}