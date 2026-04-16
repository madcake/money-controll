package shiny.mc.core.ports.app_config

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.PeriodDate

interface GetCurrentPeriodDate {
    fun period(): Flow<PeriodDate>
}