package shiny.mc.core.coordinators.app_config

import kotlinx.datetime.DatePeriod

interface SetConfigValue {
    suspend fun currentPeriod(datePeriod: DatePeriod)
}