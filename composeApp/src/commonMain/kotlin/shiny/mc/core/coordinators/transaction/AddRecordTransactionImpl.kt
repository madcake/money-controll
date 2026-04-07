package shiny.mc.core.coordinators.transaction

import shiny.mc.core.domain.entity.Transaction
import shiny.mc.core.repositories.TransactionRepository

class AddRecordTransactionImpl(
    private val transactionRepository: TransactionRepository,
) : AddRecordTransaction {
    override suspend fun addTransaction(
        recordId: String,
        value: Double,
        purpose: String,
        datetime: Long
    ) {
        transactionRepository.addRecordTransaction(
            recordId = recordId,
            transaction = Transaction(
                 purpose = purpose,
                value = value,
                datetime = datetime,
            )
        )
    }
}