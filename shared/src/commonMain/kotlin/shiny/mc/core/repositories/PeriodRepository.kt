package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.Period

interface PeriodRepository {
    fun getPeriods(): Flow<List<Period>>
}