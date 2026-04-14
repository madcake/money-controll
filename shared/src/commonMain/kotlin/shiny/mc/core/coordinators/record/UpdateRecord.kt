package shiny.mc.core.coordinators.record

interface UpdateRecordValue {
    suspend fun update(recordId: String, estimateValue: String)
}