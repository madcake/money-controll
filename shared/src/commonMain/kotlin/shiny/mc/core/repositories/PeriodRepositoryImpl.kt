package shiny.mc.core.repositories

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.value.Period
import shiny.mc.services.store.dao.PeriodDao
import shiny.mc.services.store.entity.toDto

class PeriodRepositoryImpl(
    private val periodDao: PeriodDao,
) : PeriodRepository {

    override fun getPeriods(): Flow<List<Period>> {
        return periodDao.getPeriods().toDto()
    }
}