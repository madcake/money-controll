package shiny.mc.core.coordinators.app_config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import shiny.mc.core.adapters.AppConfigRepository
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.ports.app_config.SetCurrentPeriod

class SetCurrentPeriodImpl(
    private val appConfigRepository: AppConfigRepository,
) : SetCurrentPeriod {

    override suspend fun period(period: PeriodDate) = withContext(Dispatchers.IO) {
        appConfigRepository.currentPeriod(period)
    }
}