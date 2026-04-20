package shiny.mc.feature.transaction.ports

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.adapters.TransactionRepository
import shiny.mc.core.ports.transaction.GetTransactionSuggestions

class GetTransactionSuggestionsImpl(
    private val transactionRepository: TransactionRepository,
    private val recordRepository: RecordRepository,
) : GetTransactionSuggestions {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun purposeSuggestions(
        recordId: String,
        query: String
    ): Flow<List<String>> {
        return recordRepository.getRecord(recordId)
            .mapLatest { it?.category?.id }
            .filterNotNull()
            .flatMapLatest { transactionRepository.getSuggestions(it, query) }
    }
}