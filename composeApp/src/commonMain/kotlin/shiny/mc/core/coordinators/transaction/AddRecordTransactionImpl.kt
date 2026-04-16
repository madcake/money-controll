package shiny.mc.core.coordinators.transaction

import shiny.mc.core.adapters.TransactionRepository
import shiny.mc.core.dto.Transaction
import shiny.mc.core.ports.transaction.AddRecordTransaction
import shiny.mc.core.ports.transaction.TransactionValidator
import shiny.mc.platform.parseToDouble

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
                value = value.parseToDouble(),
                datetime = datetime,
            )
        )
    }
}