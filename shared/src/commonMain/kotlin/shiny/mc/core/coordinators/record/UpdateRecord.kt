package shiny.mc.core.coordinators.record

import shiny.mc.core.domain.aggregate.CategoryRecord

interface UpdateRecord {
    fun update(record: CategoryRecord)
}