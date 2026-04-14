package shiny.mc.core.coordinators.transaction

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.repositories.CategoryRepository
import shiny.mc.core.repositories.TransactionRepository

class GetTransactionSuggestionsImpl(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
) : GetTransactionSuggestions {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun purposeSuggestions(
        recordId: String,
        query: String
    ): Flow<List<String>> {
        return categoryRepository.getRecord(recordId)
            .mapLatest { it?.category?.id }
            .filterNotNull()
            .flatMapLatest {
                transactionRepository.getSuggestions(it, query)
            }
    }
}