package shiny.mc.core.coordinators.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.adapters.PeriodRepository
import shiny.mc.core.dto.Period
import shiny.mc.core.ports.period.GetPeriods

class GetPeriodsImpl(
    private val periodRepository: PeriodRepository,
) : GetPeriods {

    override fun periods(): Flow<List<Period>> {
        return periodRepository.getPeriods()
    }
}