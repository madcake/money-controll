package shiny.mc.core.coordinators.period

import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.core.repositories.RecordRepository

class CopyPeriodImpl(
    private val recordRepository: RecordRepository,
) : CopyPeriod {
    override suspend fun copy(
        from: PeriodDate,
        to: PeriodDate
    ) {
        recordRepository.copyPeriod(from, to)
    }
}