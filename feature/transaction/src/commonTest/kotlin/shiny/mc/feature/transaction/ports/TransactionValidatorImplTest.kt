package shiny.mc.feature.transaction.ports

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import shiny.mc.core.domain.entity.RecordInfo
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.ValueState
import shiny.mc.core.dto.error.TransactionError
import shiny.mc.core.ports.record.GetRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TransactionValidatorImplTest {

    private class TestGetRecord(
        val onGetRecord: (String) -> Flow<RecordInfo?>
    ) : GetRecord {
        override fun getRecord(recordId: String): Flow<RecordInfo?> = onGetRecord(recordId)
    }

    private data class TestRecordInfo(
        override val id: String = "recordId",
        override val categoryId: Long = 1L,
        override val categoryType: CategoryType = CategoryType.Out,
        override val title: String = "Title",
        override val estimateValue: Double = 100.0,
        override val estimateValueFormatted: String = "100.0",
        override val realValue: String = "0.0",
        override val valueState: ValueState = ValueState.Deficit,
        override val period: PeriodDate = PeriodDate(10, 2023),
        override val icon: Any = Any()
    ) : RecordInfo

    private val recordId = "record-1"
    private val period = PeriodDate(10, 2023)
    // 2023-10-15T12:00:00Z
    private val validDateMillis = LocalDateTime(2023, 10, 15, 12, 0)
        .toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()
        .toString()

    @Test
    fun validate_returnsTrue_whenAllFieldsAreValid() = runTest {
        val getRecord = TestGetRecord { id ->
            assertEquals(recordId, id)
            flowOf(TestRecordInfo(id = id, period = period))
        }
        val validator = TransactionValidatorImpl(getRecord)

        val result = validator.validate(
            recordId = recordId,
            value = "50.5",
            purpose = "Coffee",
            date = validDateMillis
        )

        assertTrue(result)
    }

    @Test
    fun validate_throwsIncorrectValue_whenValueIsInvalid() = runTest {
        val validator = TransactionValidatorImpl(TestGetRecord { flowOf(null) })

        assertFailsWith<TransactionError.IncorrectValue> {
            validator.validate(recordId, value = "invalid", purpose = "Coffee", date = validDateMillis)
        }
        assertFailsWith<TransactionError.IncorrectValue> {
            validator.validate(recordId, value = "-10", purpose = "Coffee", date = validDateMillis)
        }
        assertFailsWith<TransactionError.IncorrectValue> {
            validator.validate(recordId, value = null, purpose = "Coffee", date = validDateMillis)
        }
    }

    @Test
    fun validate_throwsIncorrectPurpose_whenPurposeIsEmptyOrNull() = runTest {
        val validator = TransactionValidatorImpl(TestGetRecord { flowOf(null) })

        assertFailsWith<TransactionError.IncorrectPurpose> {
            validator.validate(recordId, value = "10", purpose = "", date = validDateMillis)
        }
        assertFailsWith<TransactionError.IncorrectPurpose> {
            validator.validate(recordId, value = "10", purpose = null, date = validDateMillis)
        }
    }

    @Test
    fun validate_throwsIncorrectRecord_whenRecordNotFound() = runTest {
        val getRecord = TestGetRecord { flowOf(null) }
        val validator = TransactionValidatorImpl(getRecord)

        assertFailsWith<TransactionError.IncorrectRecord> {
            validator.validate(recordId, value = "10", purpose = "Coffee", date = validDateMillis)
        }
    }

    @Test
    fun validate_throwsInvalidDate_whenDateIsOutsidePeriod() = runTest {
        val getRecord = TestGetRecord { id ->
            flowOf(TestRecordInfo(id = id, period = period))
        }
        val validator = TransactionValidatorImpl(getRecord)

        // Wrong month
        val wrongMonthDate = LocalDateTime(2023, 11, 15, 12, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
            .toString()

        assertFailsWith<TransactionError.InvalidDate> {
            validator.validate(recordId, value = "10", purpose = "Coffee", date = wrongMonthDate)
        }

        // Wrong year
        val wrongYearDate = LocalDateTime(2022, 10, 15, 12, 0)
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
            .toString()

        assertFailsWith<TransactionError.InvalidDate> {
            validator.validate(recordId, value = "10", purpose = "Coffee", date = wrongYearDate)
        }
    }

    @Test
    fun validate_throwsInvalidDate_whenDateIsNull() = runTest {
        val getRecord = TestGetRecord { id ->
            flowOf(TestRecordInfo(id = id, period = period))
        }
        val validator = TransactionValidatorImpl(getRecord)

        assertFailsWith<TransactionError.InvalidDate> {
            validator.validate(recordId, value = "10", purpose = "Coffee", date = null)
        }
    }
}
