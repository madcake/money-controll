package shiny.mc.core.coordinators.app_config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.core.repositories.AppConfigRepository

class SetCurrentPeriodImpl(
    private val appConfigRepository: AppConfigRepository,
) : SetCurrentPeriod {

    override suspend fun period(period: PeriodDate) = withContext(Dispatchers.IO) {
        appConfigRepository.currentPeriod(period)
    }
}