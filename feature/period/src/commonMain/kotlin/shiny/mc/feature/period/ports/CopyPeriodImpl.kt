package shiny.mc.feature.period.ports

import kotlinx.coroutines.flow.firstOrNull
import shiny.mc.core.adapters.PeriodRepository
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.error.PeriodError
import shiny.mc.core.ports.app_config.SetCurrentPeriod
import shiny.mc.core.ports.period.CopyPeriod

/**
 * Implementation of [CopyPeriod] port.
 *
 * This implementation copies a period from one date to another using the [PeriodRepository].
 * After a successful copy, it updates the current active period to the new date.
 *
 * @property periodRepository Repository for period operations.
 * @property currentPeriod Port for setting the current active period.
 */
class CopyPeriodImpl(
    private val periodRepository: PeriodRepository,
    private val currentPeriod: SetCurrentPeriod,
) : CopyPeriod {

    /**
     * Copies data from one period to another.
     *
     * @param from The source period date.
     * @param to The destination period date.
     * @throws PeriodError.PeriodNotFound if the source period does not exist.
     * @throws PeriodError.PeriodExists if the destination period already exists.
     */
    override suspend fun copy(
        from: PeriodDate,
        to: PeriodDate
    ) {
        periodRepository.getPeriod(from).firstOrNull() ?:
            throw PeriodError.PeriodNotFound(from)

        if (periodRepository.getPeriod(to).firstOrNull() != null) {
            throw PeriodError.PeriodExists(to)
        }

        periodRepository.copyPeriod(from, to)
        currentPeriod.period(to)
    }
}