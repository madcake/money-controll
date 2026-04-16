package shiny.mc.infrastructure.persistent.room.adapters

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.adapters.PeriodRepository
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate
import shiny.mc.infrastructure.persistent.room.dao.PeriodDao
import shiny.mc.infrastructure.persistent.room.entity.toDto

class RoomPeriodRepository(
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