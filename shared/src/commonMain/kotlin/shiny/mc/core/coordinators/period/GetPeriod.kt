package shiny.mc.core.coordinators.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.Period
import shiny.mc.core.domain.value.PeriodDate

interface GetPeriod {
    fun period(date: PeriodDate): Flow<Period?>
}