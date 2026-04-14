package shiny.mc.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel
import shiny.mc.core.coordinators.app_config.GetCurrentPeriodDate
import shiny.mc.core.coordinators.app_config.GetCurrentPeriodDateImpl
import shiny.mc.core.coordinators.app_config.SetCurrentPeriod
import shiny.mc.core.coordinators.app_config.SetCurrentPeriodImpl
import shiny.mc.core.coordinators.category.AddCategory
import shiny.mc.core.coordinators.category.AddCategoryImpl
import shiny.mc.core.coordinators.category.CategoryInputValidator
import shiny.mc.core.coordinators.category.CategoryInputValidatorImpl
import shiny.mc.core.coordinators.category.DeleteCategory
import shiny.mc.core.coordinators.category.DeleteCategoryImpl
import shiny.mc.core.coordinators.category.SearchCategories
import shiny.mc.core.coordinators.category.SearchCategoriesImpl
import shiny.mc.core.coordinators.period.GetPeriod
import shiny.mc.core.coordinators.period.GetPeriodImpl
import shiny.mc.core.coordinators.period.GetPeriods
import shiny.mc.core.coordinators.period.GetPeriodsImpl
import shiny.mc.core.coordinators.record.AddRecords
import shiny.mc.core.coordinators.record.ChangeRecords
import shiny.mc.core.coordinators.record.GetPeriodRecords
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.record.UpdateRecordValue
import shiny.mc.core.coordinators.records.AddRecordsImpl
import shiny.mc.core.coordinators.records.ChangeRecordsImpl
import shiny.mc.core.coordinators.records.GetPeriodRecordsImpl
import shiny.mc.core.coordinators.records.GetRecordImpl
import shiny.mc.core.coordinators.records.UpdateRecordValueImpl
import shiny.mc.core.coordinators.transaction.AddRecordTransaction
import shiny.mc.core.coordinators.transaction.AddRecordTransactionImpl
import shiny.mc.core.coordinators.transaction.DeleteTransaction
import shiny.mc.core.coordinators.transaction.DeleteTransactionImpl
import shiny.mc.core.coordinators.transaction.GetRecordTransactions
import shiny.mc.core.coordinators.transaction.GetRecordTransactionsImpl
import shiny.mc.core.coordinators.transaction.GetTransactionSuggestions
import shiny.mc.core.coordinators.transaction.GetTransactionSuggestionsImpl
import shiny.mc.core.coordinators.transaction.TransactionValidator
import shiny.mc.core.coordinators.transaction.TransactionValidatorImpl
import shiny.mc.core.repositories.AppConfigRepository
import shiny.mc.core.repositories.AppConfigRepositoryImpl
import shiny.mc.core.repositories.CategoryRepository
import shiny.mc.core.repositories.CategoryRepositoryImpl
import shiny.mc.core.repositories.PeriodRepository
import shiny.mc.core.repositories.PeriodRepositoryImpl
import shiny.mc.core.repositories.TransactionRepository
import shiny.mc.core.repositories.TransactionRepositoryImpl
import shiny.mc.feature.add_category.AddCategoryViewModel
import shiny.mc.feature.add_transaction.AddTransactionViewModel
import shiny.mc.feature.categories.CategoriesViewModel
import shiny.mc.feature.period.PeriodViewModel
import shiny.mc.feature.periods.PeriodsViewModel
import shiny.mc.feature.record.EditRecordViewModel
import shiny.mc.feature.record.RecordViewModel
import shiny.mc.services.store.RoomStore
import shiny.mc.services.store.dao.AppConfigDao
import shiny.mc.services.store.dao.CategoryDao
import shiny.mc.services.store.dao.PeriodDao
import shiny.mc.services.store.dao.RecordDao
import shiny.mc.services.store.dao.TransactionDao

expect val platformModule: Module

val storeModule = module {
    single<RoomStore> { create(::getRoomDatabase) }
    single<CategoryDao> { create(::getCategoryDao) }
    single<RecordDao> { create(::getCategoryRecordDao) }
    single<TransactionDao> { create(::getTransactionDao) }
    single<PeriodDao> { create(::getPeriodDao) }
    single<AppConfigDao> { create(::getAppConfigDao) }
}

val repositoryModule = module {
    includes(storeModule)
    single<AppConfigRepositoryImpl>() bind AppConfigRepository::class
    single<CategoryRepositoryImpl>() bind CategoryRepository::class
    single<TransactionRepositoryImpl>() bind TransactionRepository::class
    single<PeriodRepositoryImpl>() bind PeriodRepository::class
}

val coordinateModule = module {
    includes(repositoryModule)

    single<SetCurrentPeriodImpl>() bind SetCurrentPeriod::class
    single<GetCurrentPeriodDateImpl>() bind GetCurrentPeriodDate::class

    single<AddCategoryImpl>() bind AddCategory::class
    single<CategoryInputValidatorImpl>() bind CategoryInputValidator::class
    single<SearchCategoriesImpl>() bind SearchCategories::class
    single<DeleteCategoryImpl>() bind DeleteCategory::class

    single<GetPeriodRecordsImpl>() bind GetPeriodRecords::class
    single<AddRecordsImpl>() bind AddRecords::class
    single<GetRecordImpl>() bind GetRecord::class
    single<UpdateRecordValueImpl>() bind UpdateRecordValue::class
    single<ChangeRecordsImpl>() bind ChangeRecords::class

    single<GetRecordTransactionsImpl>() bind GetRecordTransactions::class
    single<GetTransactionSuggestionsImpl>() bind GetTransactionSuggestions::class
    single<TransactionValidatorImpl>() bind TransactionValidator::class
    single<AddRecordTransactionImpl>() bind AddRecordTransaction::class
    single<DeleteTransactionImpl>() bind DeleteTransaction::class

    single<GetPeriodsImpl>() bind GetPeriods::class
    single<GetPeriodImpl>() bind GetPeriod::class
}

val viewModelModule = module {
    viewModel<PeriodViewModel>()
    viewModel<AddCategoryViewModel>()
    viewModel<CategoriesViewModel>()
    viewModel<RecordViewModel>()
    viewModel<AddTransactionViewModel>()
    viewModel<EditRecordViewModel>()
    viewModel<PeriodsViewModel>()
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<RoomStore>,
): RoomStore {
    return builder
        .addMigrations()
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

fun getCategoryDao(store: RoomStore): CategoryDao {
    return store.categoryDao()
}

fun getCategoryRecordDao(store: RoomStore): RecordDao {
    return store.categoryRecordDao()
}

fun getTransactionDao(store: RoomStore): TransactionDao {
    return store.transactionDao()
}

fun getPeriodDao(store: RoomStore): PeriodDao {
    return store.periodDao()
}

fun getAppConfigDao(store: RoomStore): AppConfigDao {
    return store.appConfigDao()
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    logger(object : Logger() {
        override fun display(level: Level, msg: MESSAGE) {
            println("${level.name.uppercase()}: $msg")
        }
    })
    modules(
        viewModelModule,
        coordinateModule,
        platformModule,
    )
}

// called by iOS
fun initKoinIos() = initKoin {}