package shiny.mc.core.coordinators.transaction

import kotlinx.coroutines.flow.Flow
import shiny.mc.core.domain.entity.Transaction

interface GetRecordTransactions {
    fun transactions(recordId: String): Flow<List<Transaction>>
}