package shiny.mc.core.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.value.Period
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.services.store.dao.PeriodDao
import shiny.mc.services.store.entity.toDto

class PeriodRepositoryImpl(
    private val periodDao: PeriodDao,
) : PeriodRepository {

    override fun getPeriods(): Flow<List<Period>> {
        return periodDao.getPeriods().toDto()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPeriod(date: PeriodDate): Flow<Period?> {
        return periodDao.getPeriod(date.month, date.year).mapLatest { it?.toDto() }
    }
    override suspend fun copyPeriod(
        from: PeriodDate,
        to: PeriodDate
    ) {
        periodDao.copy(from.month, from.year, to.month, to.year)
    }
}