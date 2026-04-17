package shiny.mc.di

import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel
import shiny.mc.core.coordinators.app_config.GetCurrentPeriodDateImpl
import shiny.mc.core.coordinators.app_config.SetCurrentPeriodImpl
import shiny.mc.core.ports.app_config.GetCurrentPeriodDate
import shiny.mc.core.ports.app_config.SetCurrentPeriod
import shiny.mc.core.ports.category.AddCategory
import shiny.mc.core.ports.category.DeleteCategory
import shiny.mc.core.ports.category.SearchCategories
import shiny.mc.core.ports.period.CopyPeriod
import shiny.mc.core.ports.period.GetPeriod
import shiny.mc.core.ports.period.GetPeriods
import shiny.mc.core.ports.record.AddRecords
import shiny.mc.core.ports.record.ChangeRecords
import shiny.mc.core.ports.record.GetPeriodRecords
import shiny.mc.core.ports.record.GetRecord
import shiny.mc.core.ports.record.UpdateRecordValue
import shiny.mc.core.ports.transaction.AddRecordTransaction
import shiny.mc.core.ports.transaction.DeleteTransaction
import shiny.mc.core.ports.transaction.GetRecordTransactions
import shiny.mc.core.ports.transaction.GetTransactionSuggestions
import shiny.mc.core.ports.transaction.TransactionValidator
import shiny.mc.feature.category.ports.AddCategoryImpl
import shiny.mc.feature.category.ports.DeleteCategoryImpl
import shiny.mc.feature.category.ports.SearchCategoriesImpl
import shiny.mc.feature.category.presentation.add_category.AddCategoryViewModel
import shiny.mc.feature.category.presentation.categories.CategoriesViewModel
import shiny.mc.feature.period.ports.CopyPeriodImpl
import shiny.mc.feature.period.ports.GetPeriodImpl
import shiny.mc.feature.period.ports.GetPeriodsImpl
import shiny.mc.feature.period.presentation.add_period.AddPeriodViewModel
import shiny.mc.feature.period.presentation.period.PeriodViewModel
import shiny.mc.feature.period.presentation.periods.PeriodsViewModel
import shiny.mc.feature.record.ports.AddRecordsImpl
import shiny.mc.feature.record.ports.ChangeRecordsImpl
import shiny.mc.feature.record.ports.GetPeriodRecordsImpl
import shiny.mc.feature.record.ports.GetRecordImpl
import shiny.mc.feature.record.ports.UpdateRecordValueImpl
import shiny.mc.feature.record.presentation.EditRecordViewModel
import shiny.mc.feature.record.presentation.RecordViewModel
import shiny.mc.feature.transaction.add_transaction.AddTransactionViewModel
import shiny.mc.feature.transaction.ports.AddRecordTransactionImpl
import shiny.mc.feature.transaction.ports.DeleteTransactionImpl
import shiny.mc.feature.transaction.ports.GetRecordTransactionsImpl
import shiny.mc.feature.transaction.ports.GetTransactionSuggestionsImpl
import shiny.mc.feature.transaction.ports.TransactionValidatorImpl
import shiny.mc.infrastructure.persistent.room.di.platformStoreModule
import shiny.mc.infrastructure.persistent.room.di.repositoryModule
import shiny.mc.infrastructure.persistent.room.di.storeModule

val coordinateModule = module {
//    includes(repositoryModule)

    single<SetCurrentPeriodImpl>() bind SetCurrentPeriod::class
    single<GetCurrentPeriodDateImpl>() bind GetCurrentPeriodDate::class

    single<AddCategoryImpl>() bind AddCategory::class
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
    single<CopyPeriodImpl>() bind CopyPeriod::class
}

val viewModelModule = module {
    viewModel<PeriodViewModel>()
    viewModel<AddCategoryViewModel>()
    viewModel<CategoriesViewModel>()
    viewModel<RecordViewModel>()
    viewModel<AddTransactionViewModel>()
    viewModel<EditRecordViewModel>()
    viewModel<PeriodsViewModel>()
    viewModel<AddPeriodViewModel>()
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
        repositoryModule,
        storeModule,
        platformStoreModule,
    )
}

// called by iOS
fun initKoinIos() = initKoin {}