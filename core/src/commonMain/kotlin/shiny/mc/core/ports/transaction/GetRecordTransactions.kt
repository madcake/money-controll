package shiny.mc.core.ports.transaction

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.TransactionInfo
import shiny.mc.core.dto.Transaction

/**
 * Port for retrieving all transactions associated with a specific record.
 */
interface GetRecordTransactions {
    /**
     * Retrieves a list of transactions for the given record ID.
     *
     * @param recordId The unique identifier of the record.
     * @return A [Flow] emitting the list of [Transaction]s for that record.
     */
    fun transactions(recordId: String): Flow<List<TransactionInfo>>
}