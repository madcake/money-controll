package shiny.mc.infrastructure.persistent.room.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
import shiny.mc.core.adapters.AppConfigRepository
import shiny.mc.core.adapters.CategoryRepository
import shiny.mc.core.adapters.PeriodRepository
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.adapters.TransactionRepository
import shiny.mc.infrastructure.persistent.room.RoomStore
import shiny.mc.infrastructure.persistent.room.adapters.RoomAppConfigRepository
import shiny.mc.infrastructure.persistent.room.adapters.RoomCategoryRepository
import shiny.mc.infrastructure.persistent.room.adapters.RoomPeriodRepository
import shiny.mc.infrastructure.persistent.room.adapters.RoomRecordRepository
import shiny.mc.infrastructure.persistent.room.adapters.RoomTransactionRepository
import shiny.mc.infrastructure.persistent.room.dao.AppConfigDao
import shiny.mc.infrastructure.persistent.room.dao.CategoryDao
import shiny.mc.infrastructure.persistent.room.dao.PeriodDao
import shiny.mc.infrastructure.persistent.room.dao.RecordDao
import shiny.mc.infrastructure.persistent.room.dao.TransactionDao

const val DATABASE_NAME = "store.db"

expect val platformStoreModule: Module

val storeModule = module {
    single<RoomStore> { create(::getRoomDatabase) }
    single<AppConfigDao> { create(::getAppConfigDao) }
    single<CategoryDao> { create(::getCategoryDao) }
    single<RecordDao> { create(::getRecordDao) }
    single<TransactionDao> { create(::getTransactionDao) }
    single<PeriodDao> { create(::getPeriodDao) }
}

val repositoryModule = module {
    single<RoomAppConfigRepository>() bind AppConfigRepository::class
    single<RoomCategoryRepository>() bind CategoryRepository::class
    single<RoomRecordRepository>() bind RecordRepository::class
    single<RoomTransactionRepository>() bind TransactionRepository::class
    single<RoomPeriodRepository>() bind PeriodRepository::class
}

internal fun getRoomDatabase(
    builder: RoomDatabase.Builder<RoomStore>,
): RoomStore {
    return builder
        .addMigrations()
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

internal fun getAppConfigDao(source: RoomStore): AppConfigDao = source.appConfigDao()
internal fun getCategoryDao(source: RoomStore): CategoryDao = source.categoryDao()
internal fun getRecordDao(source: RoomStore): RecordDao = source.categoryRecordDao()
internal fun getTransactionDao(source: RoomStore): TransactionDao = source.transactionDao()
internal fun getPeriodDao(source: RoomStore): PeriodDao = source.periodDao()
