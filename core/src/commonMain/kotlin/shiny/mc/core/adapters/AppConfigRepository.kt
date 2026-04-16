package shiny.mc.core.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.PeriodDate

interface AppConfigRepository {

    suspend fun currentPeriod(date: PeriodDate)

    fun currentPeriod(): Flow<PeriodDate>
}