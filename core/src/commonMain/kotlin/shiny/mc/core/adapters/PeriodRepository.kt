package shiny.mc.core.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate

interface PeriodRepository {
    fun getPeriods(): Flow<List<Period>>

    fun getPeriod(date: PeriodDate): Flow<Period?>

    /**
     * Copy period records and configs to new period
     *
     * @param from copy from period
     * @param to distance to copy
     * @throws shiny.mc.core.dto.error.RecordError
     */
    suspend fun copyPeriod(from: PeriodDate, to: PeriodDate)
}