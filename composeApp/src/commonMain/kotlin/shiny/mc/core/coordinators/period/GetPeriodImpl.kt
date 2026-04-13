package shiny.mc.core.coordinators.period

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.Period
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.core.repositories.PeriodRepository

class GetPeriodImpl(
    private val periodRepository: PeriodRepository,
) : GetPeriod {

    override fun period(date: PeriodDate): Flow<Period?> {
        return periodRepository.getPeriod(date)
    }
}