package shiny.mc.core.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.services.store.dao.AppConfigDao
import shiny.mc.services.store.entity.AppConfigEntity
import shiny.mc.services.store.entity.AppConfigKeyEntity

class AppConfigRepositoryImpl(
    private val appConfigDao: AppConfigDao
) : AppConfigRepository {

    override suspend fun currentPeriod(date: PeriodDate) {
        put(AppConfigKeyEntity.CurrentPeriod, date.toMillis().toString())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun currentPeriod(): Flow<PeriodDate> {
        return appConfigDao.get(AppConfigKeyEntity.CurrentPeriod).mapLatest { value ->
            value?.toLong()?.let { PeriodDate.fromMillis(it) } ?: PeriodDate.default()
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