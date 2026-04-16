package shiny.mc.infrastructure.persistent.room.adapters

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import shiny.mc.core.adapters.AppConfigRepository
import shiny.mc.core.dto.PeriodDate
import shiny.mc.infrastructure.persistent.room.dao.AppConfigDao

class RoomAppConfigRepository(
    private val appConfigDao: AppConfigDao
) : AppConfigRepository {

    override suspend fun currentPeriod(date: PeriodDate) {
//        put(AppConfigKeyEntity.CurrentPeriod, date.toMillis().toString())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun currentPeriod(): Flow<PeriodDate> {
//        return appConfigDao.get(AppConfigKeyEntity.CurrentPeriod).mapLatest { value ->
//            value?.toLong()?.let { PeriodDate.fromMillis(it) } ?: PeriodDate.default()
//        }
        return flow {
            emit(PeriodDate(4, 2026))
        }
    }
}