package shiny.mc.core.ports.transaction

import kotlinx.coroutines.flow.Flow

/**
 * Port for retrieving transaction purpose suggestions based on previous entries.
 */
interface GetTransactionSuggestions {
    /**
     * Provides a list of suggested purposes for a transaction based on a partial query.
     *
     * @param recordId The ID of the record context.
     * @param query The partial string to find matching suggestions for.
     * @return A [Flow] emitting a list of suggested strings.
     */
    suspend fun purposeSuggestions(recordId: String, query: String): Flow<List<String>>
}