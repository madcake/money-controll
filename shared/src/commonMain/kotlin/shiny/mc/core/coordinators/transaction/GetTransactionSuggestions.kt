package shiny.mc.core.coordinators.transaction

import kotlinx.coroutines.flow.Flow

interface GetTransactionSuggestions {
    suspend fun purposeSuggestions(recordId: String, query: String): Flow<List<String>>
}