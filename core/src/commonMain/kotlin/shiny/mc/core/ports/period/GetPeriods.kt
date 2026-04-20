package shiny.mc.core.ports.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Period

/**
 * Port for retrieving all available transaction periods.
 */
interface GetPeriods {
    /**
     * Retrieves a list of all periods.
     *
     * @return A [Flow] emitting a list of all [Period]s.
     */
    fun periods(): Flow<List<Period>>
}