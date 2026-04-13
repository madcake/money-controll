package shiny.mc.core.coordinators.app_config

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.core.repositories.AppConfigRepository

class GetCurrentPeriodDateImpl(
    private val appConfigRepository: AppConfigRepository,
) : GetCurrentPeriodDate {
    override fun period(): Flow<PeriodDate> {
        return appConfigRepository.currentPeriod()
    }
}