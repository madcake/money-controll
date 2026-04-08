package shiny.mc.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.viewModel
import shiny.mc.SimpleViewModel
import shiny.mc.core.coordinators.category.AddCategory
import shiny.mc.core.coordinators.category.AddCategoryImpl
import shiny.mc.core.coordinators.category.CategoryInputValidator
import shiny.mc.core.coordinators.category.CategoryInputValidatorImpl
import shiny.mc.core.coordinators.category.DeleteCategory
import shiny.mc.core.coordinators.category.DeleteCategoryImpl
import shiny.mc.core.coordinators.category.SearchCategories
import shiny.mc.core.coordinators.category.SearchCategoriesImpl
import shiny.mc.core.coordinators.record.AddRecords
import shiny.mc.core.coordinators.record.ChangeRecords
import shiny.mc.core.coordinators.record.GetPeriodRecords
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.record.UpdateRecord
import shiny.mc.core.coordinators.records.AddRecordsImpl
import shiny.mc.core.coordinators.records.ChangeRecordsImpl
import shiny.mc.core.coordinators.records.GetPeriodRecordsImpl
import shiny.mc.core.coordinators.records.GetRecordImpl
import shiny.mc.core.coordinators.records.UpdateRecordImpl
import shiny.mc.core.coordinators.transaction.AddRecordTransaction
import shiny.mc.core.coordinators.transaction.AddRecordTransactionImpl
import shiny.mc.core.coordinators.transaction.GetRecordTransactions
import shiny.mc.core.coordinators.transaction.GetRecordTransactionsImpl
import shiny.mc.core.repositories.CategoryRepository
import shiny.mc.core.repositories.CategoryRepositoryImpl
import shiny.mc.core.repositories.TransactionRepository
import shiny.mc.core.repositories.TransactionRepositoryImpl
import shiny.mc.feature.add_category.AddCategoryViewModel
import shiny.mc.feature.add_expense.AddExpenseViewModel
import shiny.mc.feature.add_transaction.AddTransactionViewModel
import shiny.mc.feature.categories.CategoriesViewModel
import shiny.mc.feature.period.PeriodViewModel
import shiny.mc.feature.record.EditRecordViewModel
import shiny.mc.feature.record.RecordViewModel
import shiny.mc.services.store.RoomStore
import shiny.mc.services.store.dao.CategoryDao
import shiny.mc.services.store.dao.ExpenseDao
import shiny.mc.services.store.dao.RecordDao
import shiny.mc.services.store.dao.TransactionDao

expect val platformModule: Module

val storeModule = module {
    single<RoomStore> { create(::getRoomDatabase) }
    single<ExpenseDao> { create(::getExpenseDao) }
    single<CategoryDao> { create(::getCategoryDao) }
    single<RecordDao> { create(::getCategoryRecordDao) }
    single<TransactionDao> { create(::getTransactionDao) }
}

val repositoryModule = module {
    single<CategoryRepository> { create(::CategoryRepositoryImpl) }
    single<TransactionRepository> { create(::TransactionRepositoryImpl) }
}

val coordinateModule = module {
    single<AddCategory> { create(::AddCategoryImpl) }
    single<CategoryInputValidator> { create(::CategoryInputValidatorImpl) }
    single<SearchCategories> { create(::SearchCategoriesImpl) }
    single<DeleteCategory> { create(::DeleteCategoryImpl) }
    single<GetPeriodRecords> { create(::GetPeriodRecordsImpl) }
    single<AddRecords> { create(::AddRecordsImpl) }
    single<GetRecord> { create(::GetRecordImpl) }
    single<GetRecordTransactions> { create(::GetRecordTransactionsImpl) }
    single<AddRecordTransaction> { create(::AddRecordTransactionImpl) }
    single<UpdateRecord> { create(::UpdateRecordImpl) }
    single<ChangeRecords> { create(::ChangeRecordsImpl) }
}

val viewModelModule = module {
    viewModel<SimpleViewModel>()
    viewModel<PeriodViewModel>()
    viewModel<AddExpenseViewModel>()
    viewModel<AddCategoryViewModel>()
    viewModel<CategoriesViewModel>()
    viewModel<RecordViewModel>()
    viewModel<AddTransactionViewModel>()
    viewModel<EditRecordViewModel>()
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

fun getExpenseDao(store: RoomStore): ExpenseDao {
    return store.expenseDao()
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

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        repositoryModule,
        coordinateModule,
        viewModelModule,
        storeModule,
        platformModule,
    )
}

// called by iOS
fun initKoinIos() = initKoin {}