package shiny.mc.feature.period.ports

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import shiny.mc.core.adapters.PeriodRepository
import shiny.mc.core.dto.Period
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.PeriodValues
import shiny.mc.core.dto.error.PeriodError
import shiny.mc.core.ports.app_config.SetCurrentPeriod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CopyPeriodImplTest {

    private class MockPeriodRepository : PeriodRepository {
        var periods = mutableMapOf<PeriodDate, Period?>()
        var copyPeriodCalled = false
        var lastFrom: PeriodDate? = null
        var lastTo: PeriodDate? = null

        override fun getPeriods(): Flow<List<Period>> = flowOf(periods.values.filterNotNull())

        override fun getPeriod(date: PeriodDate): Flow<Period?> = flowOf(periods[date])

        override suspend fun copyPeriod(from: PeriodDate, to: PeriodDate) {
            copyPeriodCalled = true
            lastFrom = from
            lastTo = to
        }
    }

    private class MockSetCurrentPeriod : SetCurrentPeriod {
        var lastPeriod: PeriodDate? = null
        var called = false

        override suspend fun period(period: PeriodDate) {
            called = true
            lastPeriod = period
        }
    }

    @Test
    fun copy_success() = runTest {
        val fromDate = PeriodDate(1, 2023)
        val toDate = PeriodDate(2, 2023)
        
        val repo = MockPeriodRepository().apply {
            periods[fromDate] = Period(fromDate, PeriodValues(0.0, 0.0, 0.0, 0.0))
            periods[toDate] = null
        }
        val setCurrent = MockSetCurrentPeriod()
        val copyPeriod = CopyPeriodImpl(repo, setCurrent)

        copyPeriod.copy(fromDate, toDate)

        assertTrue(repo.copyPeriodCalled)
        assertEquals(fromDate, repo.lastFrom)
        assertEquals(toDate, repo.lastTo)
        assertEquals(toDate, setCurrent.lastPeriod)
    }

    @Test
    fun copy_throwsPeriodNotFound_whenSourceDoesNotExist() = runTest {
        val fromDate = PeriodDate(1, 2023)
        val toDate = PeriodDate(2, 2023)

        val repo = MockPeriodRepository().apply {
            periods[fromDate] = null
        }
        val setCurrent = MockSetCurrentPeriod()
        val copyPeriod = CopyPeriodImpl(repo, setCurrent)

        val exception = assertFailsWith<PeriodError.PeriodNotFound> {
            copyPeriod.copy(fromDate, toDate)
        }
        assertEquals(fromDate, exception.date)
    }

    @Test
    fun copy_throwsPeriodExists_whenDestinationAlreadyExists() = runTest {
        val fromDate = PeriodDate(1, 2023)
        val toDate = PeriodDate(2, 2023)

        val repo = MockPeriodRepository().apply {
            periods[fromDate] = Period(fromDate, PeriodValues(0.0, 0.0, 0.0, 0.0))
            periods[toDate] = Period(toDate, PeriodValues(0.0, 0.0, 0.0, 0.0))
        }
        val setCurrent = MockSetCurrentPeriod()
        val copyPeriod = CopyPeriodImpl(repo, setCurrent)

        val exception = assertFailsWith<PeriodError.PeriodExists> {
            copyPeriod.copy(fromDate, toDate)
        }
        assertEquals(toDate, exception.date)
    }
}
