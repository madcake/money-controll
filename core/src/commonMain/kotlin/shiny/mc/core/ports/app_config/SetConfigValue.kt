package shiny.mc.core.ports.app_config

import shiny.mc.core.dto.PeriodDate

interface SetConfigValue {
    suspend fun currentPeriod(datePeriod: PeriodDate)
}