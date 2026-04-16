package shiny.mc.core.ports.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate

interface GetPeriod {
    fun period(date: PeriodDate): Flow<Period?>
}