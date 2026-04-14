package shiny.mc.core.coordinators.period

import shiny.mc.core.domain.value.PeriodDate

interface CopyPeriod {
    suspend fun copy(from: PeriodDate, to: PeriodDate)
}