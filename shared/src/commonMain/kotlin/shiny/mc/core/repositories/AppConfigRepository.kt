package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.PeriodDate

interface AppConfigRepository {

    suspend fun currentPeriod(date: PeriodDate)

    fun currentPeriod(): Flow<PeriodDate>
}