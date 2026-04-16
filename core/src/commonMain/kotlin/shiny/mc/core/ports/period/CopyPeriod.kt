package shiny.mc.core.ports.period

import shiny.mc.core.dto.PeriodDate

interface CopyPeriod {
    suspend fun copy(from: PeriodDate, to: PeriodDate)
}