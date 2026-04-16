package shiny.mc.core.coordinators.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.adapters.PeriodRepository
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.ports.period.GetPeriod

class GetPeriodImpl(
    private val periodRepository: PeriodRepository,
) : GetPeriod {

    override fun period(date: PeriodDate): Flow<Period?> {
        return periodRepository.getPeriod(date)
    }
}