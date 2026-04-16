package shiny.mc.core.ports.transaction

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.dto.Transaction

interface GetRecordTransactions {
    fun transactions(recordId: String): Flow<List<Transaction>>
}