package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.Period
import shiny.mc.core.domain.value.PeriodDate

interface PeriodRepository {
    fun getPeriods(): Flow<List<Period>>

    fun getPeriod(date: PeriodDate): Flow<Period?>
}