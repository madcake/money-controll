package shiny.mc.core.ports.app_config

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.PeriodDate

/**
 * Port for retrieving the current active period date from the application configuration.
 */
interface GetCurrentPeriodDate {
    /**
     * Returns a flow of the current active period date.
     *
     * @return A [Flow] emitting the [PeriodDate] of the current active period.
     */
    fun period(): Flow<PeriodDate>
}