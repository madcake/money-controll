package shiny.mc.core.ports.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate

/**
 * Port for retrieving information about a specific period.
 */
interface GetPeriod {
    /**
     * Retrieves a period based on the given date.
     *
     * @param date The month and year of the period to retrieve.
     * @return A [Flow] emitting the [Period] if it exists, or null otherwise.
     */
    fun period(date: PeriodDate): Flow<Period?>
}