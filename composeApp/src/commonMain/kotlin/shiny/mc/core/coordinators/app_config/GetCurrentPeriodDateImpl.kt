package shiny.mc.core.coordinators.app_config

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.adapters.AppConfigRepository
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.ports.app_config.GetCurrentPeriodDate

class GetCurrentPeriodDateImpl(
    private val appConfigRepository: AppConfigRepository,
) : GetCurrentPeriodDate {
    override fun period(): Flow<PeriodDate> {
        return appConfigRepository.currentPeriod()
    }
}