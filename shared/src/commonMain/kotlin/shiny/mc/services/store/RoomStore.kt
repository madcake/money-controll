package shiny.mc.services.store

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import shiny.mc.services.store.dao.AppConfigDao
import shiny.mc.services.store.dao.CategoryDao
import shiny.mc.services.store.dao.PeriodDao
import shiny.mc.services.store.dao.RecordDao
import shiny.mc.services.store.dao.TransactionDao
import shiny.mc.services.store.entity.AppConfigEntity
import shiny.mc.services.store.entity.CategoryEntity
import shiny.mc.services.store.entity.RecordEntity
import shiny.mc.services.store.entity.TokenEntity
import shiny.mc.services.store.entity.TransactionEntity

@Database(
    entities = [
        CategoryEntity::class,
        RecordEntity::class,
        TokenEntity::class,
        TransactionEntity::class,
        AppConfigEntity::class,
    ],
    version = 1,
    exportSchema = false,
)

@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoomStore : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryRecordDao(): RecordDao
    abstract fun transactionDao(): TransactionDao
    abstract fun periodDao(): PeriodDao
    abstract fun appConfigDao(): AppConfigDao
}


@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<RoomStore> {
    override fun initialize(): RoomStore
}