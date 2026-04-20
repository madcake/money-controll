package shiny.mc.core.ports.period

import shiny.mc.core.dto.PeriodDate

/**
 * Port for copying transaction records from one period to another.
 */
interface CopyPeriod {
    /**
     * Copies records from the source period to the destination period.
     *
     * @param from The source period date.
     * @param to The destination period date.
     */
    suspend fun copy(from: PeriodDate, to: PeriodDate)
}