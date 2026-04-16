package shiny.mc.infrastructure.persistent.room.adapters

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import shiny.mc.core.adapters.AppConfigRepository
import shiny.mc.core.dto.PeriodDate
import shiny.mc.infrastructure.persistent.room.dao.AppConfigDao
import shiny.mc.infrastructure.persistent.room.entity.AppConfigEntity
import shiny.mc.infrastructure.persistent.room.entity.AppConfigKeyEntity

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

    private fun flow(key: AppConfigKeyEntity): Flow<String?> {
        return appConfigDao.get(key)
    }

    private suspend fun get(key: AppConfigKeyEntity): String? {
        return appConfigDao.get(key).firstOrNull()
    }

    private suspend fun put(
        key: AppConfigKeyEntity,
        value: String
    ) {
        appConfigDao.insert(AppConfigEntity(key, value))
    }
}