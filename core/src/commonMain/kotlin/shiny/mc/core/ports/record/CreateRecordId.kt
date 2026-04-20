package shiny.mc.core.ports.record

/**
 * Port for generating unique identifiers for records.
 */
interface CreateRecordId {
    /**
     * Creates a unique ID string for a record based on its components.
     *
     * @param categoryId The ID of the category.
     * @param month The month (1-12).
     * @param year The year.
     * @return A unique string identifier for the record.
     */
    fun createId(categoryId: Long, month: Int, year: Int): String = "${categoryId}:$month:$year"
}