package shiny.mc.core.ports.app_config

import shiny.mc.core.dto.PeriodDate

interface SetCurrentPeriod {
    suspend fun period(period: PeriodDate)
}