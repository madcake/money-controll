package shiny.mc.core.coordinators.app_config

import shiny.mc.core.domain.value.PeriodDate

interface SetCurrentPeriod {
    suspend fun period(period: PeriodDate)
}