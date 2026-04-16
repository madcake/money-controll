package shiny.mc.core.ports.record

interface CreateRecordId {
    fun createId(categoryId: Long, month: Int, year: Int): String = "${categoryId}:$month:$year"
}