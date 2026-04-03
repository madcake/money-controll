package shiny.mc.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
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
import shiny.mc.core.repositories.CategoryRepository
import shiny.mc.core.repositories.CategoryRepositoryImpl
import shiny.mc.feature.add_category.AddCategoryViewModel
import shiny.mc.feature.add_expense.AddExpenseViewModel
import shiny.mc.feature.categories.CategoriesViewModel
import shiny.mc.feature.records.ExpensesListViewModel
import shiny.mc.services.store.RoomStore
import shiny.mc.services.store.dao.CategoryDao
import shiny.mc.services.store.dao.CategoryRecordDao
import shiny.mc.services.store.dao.ExpenseDao

expect val platformModule: Module

val storeModule = module {
//    singleOf(::getRoomDatabase)
//    singleOf(::getExpenseDao)
//    singleOf(::getCategoryDao)
//    singleOf(::getCategoryRecordDao)
    single<RoomStore> { create(::getRoomDatabase) }
    single<ExpenseDao> { create(::getExpenseDao) }
    single<CategoryDao> { create(::getCategoryDao) }
    single<CategoryRecordDao> { create(::getCategoryRecordDao) }
//    single { getRoomDatabase(get()) }
//    single { get<RoomStore>().expenseDao() }
//    single { get<RoomStore>().categoryDao() }
//    single { get<RoomStore>().categoryRecordDao() }
}

val repositoryModule = module {
    single<CategoryRepository> { create(::CategoryRepositoryImpl) }
//    single<CategoryRepositoryImpl>() bind CategoryRepository::class
}

val coordinateModule = module {
    single<AddCategory> { create(::AddCategoryImpl) }
    single<CategoryInputValidator> { create(::CategoryInputValidatorImpl) }
    single<SearchCategories> { create(::SearchCategoriesImpl) }
    single<DeleteCategory> { create(::DeleteCategoryImpl) }
}

val viewModelModule = module {
    viewModel<SimpleViewModel>()
    viewModel<ExpensesListViewModel>()
    viewModel<AddExpenseViewModel>()
    viewModel<AddCategoryViewModel>()
    viewModel<CategoriesViewModel>()
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

fun getCategoryRecordDao(store: RoomStore): CategoryRecordDao {
    return store.categoryRecordDao()
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