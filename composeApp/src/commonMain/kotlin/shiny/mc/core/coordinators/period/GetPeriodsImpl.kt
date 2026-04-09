package shiny.mc.core.coordinators.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.Period
import shiny.mc.core.repositories.PeriodRepository

class GetPeriodsImpl(
    private val periodRepository: PeriodRepository,
) : GetPeriods {

    override fun periods(): Flow<List<Period>> {
        return periodRepository.getPeriods()
    }
}