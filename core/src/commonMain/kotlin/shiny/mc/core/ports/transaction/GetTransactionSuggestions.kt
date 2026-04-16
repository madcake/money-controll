package shiny.mc.core.ports.transaction

import kotlinx.coroutines.flow.Flow

interface GetTransactionSuggestions {
    suspend fun purposeSuggestions(recordId: String, query: String): Flow<List<String>>
}