package shiny.mc.core.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate

/**
 * Repository interface for managing financial periods in persistent storage.
 */
interface PeriodRepository {
    /**
     * Retrieves all available periods.
     *
     * @return A [Flow] emitting a list of all [Period] objects.
     */
    fun getPeriods(): Flow<List<Period>>

    /**
     * Retrieves a specific period by its date.
     *
     * @param date The month and year of the period.
     * @return A [Flow] emitting the [Period] if it exists, or null otherwise.
     */
    fun getPeriod(date: PeriodDate): Flow<Period?>

    /**
     * Copies all records and configuration from one period to another.
     *
     * @param from The source period date.
     * @param to The destination period date.
     * @throws shiny.mc.core.dto.error.RecordError If an error occurs during the copy process.
     */
    suspend fun copyPeriod(from: PeriodDate, to: PeriodDate)
}