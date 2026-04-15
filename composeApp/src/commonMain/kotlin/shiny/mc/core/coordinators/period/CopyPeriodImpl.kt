package shiny.mc.core.coordinators.period

import kotlinx.coroutines.flow.firstOrNull
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.core.domain.value.PeriodError
import shiny.mc.core.repositories.PeriodRepository

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