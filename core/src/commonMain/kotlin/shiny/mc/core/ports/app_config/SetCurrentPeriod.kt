package shiny.mc.core.ports.app_config

import shiny.mc.core.dto.PeriodDate

/**
 * Port for updating the current active period in the application configuration.
 */
interface SetCurrentPeriod {
    /**
     * Sets the specified period as the current active period.
     *
     * @param period The month and year to set as current.
     */
    suspend fun period(period: PeriodDate)
}