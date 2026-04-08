package shiny.mc.core.coordinators.transaction

import shiny.mc.core.domain.entity.Transaction
import shiny.mc.core.repositories.TransactionRepository

class AddRecordTransactionImpl(
    private val transactionValidator: TransactionValidator,
    private val transactionRepository: TransactionRepository,
) : AddRecordTransaction {

    override suspend fun addTransaction(
        recordId: String,
        value: String,
        purpose: String,
        datetime: Long
    ) {
        transactionValidator.validate(recordId, value, purpose, datetime.toString())
        transactionRepository.addRecordTransaction(
            recordId = recordId,
            transaction = Transaction(
                 purpose = purpose,
                value = value.toDouble(),
                datetime = datetime,
            )
        )
    }
}