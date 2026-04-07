package shiny.mc.services.store

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import shiny.mc.services.store.dao.CategoryDao
import shiny.mc.services.store.dao.CategoryRecordDao
import shiny.mc.services.store.dao.ExpenseDao
import shiny.mc.services.store.dao.TransactionDao
import shiny.mc.services.store.entity.CategoryEntity
import shiny.mc.services.store.entity.Expense
import shiny.mc.services.store.entity.RecordEntity
import shiny.mc.services.store.entity.TokenEntity
import shiny.mc.services.store.entity.TransactionEntity

@Database(
    entities = [
        Expense::class,
        CategoryEntity::class,
        RecordEntity::class,
        TokenEntity::class,
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = false,
)

@ConstructedBy(AppDatabaseConstructor::class)
abstract class RoomStore : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryRecordDao(): CategoryRecordDao
    abstract fun transactionDao(): TransactionDao
}


@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<RoomStore> {
    override fun initialize(): RoomStore
}
//
//fun getStore(
//    builder: RoomDatabase.Builder<RoomStore>,
//): RoomStore {
//    return builder
//        .addMigrations()
//        .fallbackToDestructiveMigrationOnDowngrade(true)
//        .setDriver(BundledSQLiteDriver())
//        .setQueryCoroutineContext(Dispatchers.IO)
//        .build()
//}