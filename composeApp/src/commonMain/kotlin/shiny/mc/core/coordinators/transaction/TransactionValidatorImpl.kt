package shiny.mc.core.coordinators.transaction

import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import shiny.mc.core.dto.error.TransactionError
import shiny.mc.core.ports.record.GetRecord
import shiny.mc.core.ports.transaction.TransactionValidator
import shiny.mc.platform.parseToDoubleOrNull
import kotlin.time.Instant

class TransactionValidatorImpl(
    private val getRecord: GetRecord,
) : TransactionValidator {

    override suspend fun validate(
        recordId: String,
        value: String?,
        purpose: String?,
        date: String?,
    ): Boolean {
        value?.parseToDoubleOrNull()?.let { value ->
            value.takeIf { it > 0 }
        } ?: throw TransactionError.IncorrectValue()

        if (purpose.isNullOrEmpty()) throw TransactionError.IncorrectPurpose()

        val record = getRecord.getRecord(recordId).firstOrNull() ?: throw TransactionError.IncorrectRecord()

        val dateTime = try {
            Instant.fromEpochMilliseconds(date?.toLong() ?: 0L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        } catch (_: Throwable) {
            null
        }
        if (dateTime?.date?.month?.number != record.month || dateTime.year != record.year) {
            throw TransactionError.InvalidDate()
        }

        return true
    }
}