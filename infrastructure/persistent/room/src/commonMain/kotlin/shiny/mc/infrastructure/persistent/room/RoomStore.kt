package shiny.mc.infrastructure.persistent.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import shiny.mc.infrastructure.persistent.room.dao.AppConfigDao
import shiny.mc.infrastructure.persistent.room.dao.CategoryDao
import shiny.mc.infrastructure.persistent.room.dao.PeriodDao
import shiny.mc.infrastructure.persistent.room.dao.RecordDao
import shiny.mc.infrastructure.persistent.room.dao.TransactionDao
import shiny.mc.infrastructure.persistent.room.entity.AppConfigEntity
import shiny.mc.infrastructure.persistent.room.entity.CategoryEntity
import shiny.mc.infrastructure.persistent.room.entity.RecordEntity
import shiny.mc.infrastructure.persistent.room.entity.TokenEntity
import shiny.mc.infrastructure.persistent.room.entity.TransactionEntity

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