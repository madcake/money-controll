package shiny.mc.core.ports.record

/**
 * Port for updating the estimated value of a record.
 */
interface UpdateRecordValue {
    /**
     * Updates the estimated value for the specified record.
     *
     * @param recordId The unique identifier of the record to update.
     * @param estimateValue The new estimated value as a string.
     */
    suspend fun update(recordId: String, estimateValue: String)
}