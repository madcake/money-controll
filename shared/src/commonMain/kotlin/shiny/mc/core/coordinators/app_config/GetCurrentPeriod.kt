package shiny.mc.core.coordinators.app_config

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.PeriodDate

interface GetCurrentPeriod {
    fun period(): Flow<PeriodDate>
}