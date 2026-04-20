package shiny.mc.core.adapters

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Transaction

/**
 * Repository for managing transactions.
 */
interface TransactionRepository {

    /**
     * Returns a flow of transactions for a specific record.
     *
     * @param recordId The unique identifier of the record.
     * @return Flow of a list of [Transaction]s.
     */
    fun getRecordTransactions(recordId: String): Flow<List<Transaction>>

    /**
     * Adds a transaction to a specific record.
     *
     * @param recordId The unique identifier of the record.
     * @param transaction The transaction to add.
     */
    suspend fun addRecordTransaction(recordId: String, transaction: Transaction)

    /**
     * Deletes a transaction by its ID.
     *
     * @param transactionId The unique identifier of the transaction.
     */
    suspend fun deleteTransaction(transactionId: Long)

    /**
     * Returns suggestions for transaction descriptions based on category and query.
     *
     * @param categoryId The unique identifier of the category.
     * @param query The search query string.
     * @return Flow of a list of suggestion strings.
     */
    fun getSuggestions(categoryId: Long, query: String): Flow<List<String>>
}