package shiny.mc.core.coordinators.period

import kotlinx.coroutines.flow.firstOrNull
import shiny.mc.core.adapters.PeriodRepository
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.error.PeriodError
import shiny.mc.core.ports.period.CopyPeriod

class CopyPeriodImpl(
    private val periodRepository: PeriodRepository,
) : CopyPeriod {

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
    }
}