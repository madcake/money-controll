package shiny.mc.core.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.PeriodDate

/**
 * Repository for application configuration.
 */
interface AppConfigRepository {

    /**
     * Sets the current active period.
     *
     * @param date The period date to set as current.
     */
    suspend fun currentPeriod(date: PeriodDate)

    /**
     * Returns a flow of the current active period.
     *
     * @return Flow of [PeriodDate].
     */
    fun currentPeriod(): Flow<PeriodDate>
}