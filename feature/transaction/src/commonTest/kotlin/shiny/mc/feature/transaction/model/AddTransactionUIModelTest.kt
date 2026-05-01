package shiny.mc.feature.transaction.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import shiny.mc.core.domain.entity.RecordInfo
import shiny.mc.core.dto.CategoryType
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core.dto.ValueState
import shiny.mc.core.ports.record.GetRecord
import shiny.mc.core.ports.transaction.AddRecordTransaction
import shiny.mc.core.ports.transaction.GetTransactionSuggestions
import shiny.mc.core_ui.model.CommandState
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AddTransactionUIModelTest {

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher()) // viewModelScope uses Main
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private class MockGetTransactionSuggestions : GetTransactionSuggestions {
        var suggestions = emptyList<String>()
        override suspend fun purposeSuggestions(recordId: String, query: String): Flow<List<String>> {
            return flowOf(suggestions)
        }
    }

    private class MockAddRecordTransaction : AddRecordTransaction {
        var lastRecordId: String? = null
        var lastValue: String? = null
        var lastPurpose: String? = null
        var lastDatetime: Long? = null

        override suspend fun addTransaction(
            recordId: String,
            value: String,
            purpose: String,
            datetime: Long
        ) {
            lastRecordId = recordId
            lastValue = value
            lastPurpose = purpose
            lastDatetime = datetime
        }
    }

    private class MockGetRecord : GetRecord {
        var stubbedRecord: RecordInfo? = null
        override fun getRecord(recordId: String): Flow<RecordInfo?> {
            return flowOf(stubbedRecord)
        }
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

    @Test
    fun `sendCommand ChangeRecord updates record in uiState`() = runTest(UnconfinedTestDispatcher()) {
        val mockGetRecord = MockGetRecord()
        val (uiModel, states) = createModel(backgroundScope, getRecord = mockGetRecord)
        mockGetRecord.stubbedRecord = TestRecordInfo(id = "new-id")
        uiModel.sendCommand(AddTransactionCommand.ChangeRecord("new-id"))
        val state = states.last()
        assertEquals("new-id", state.record?.id)
    }

    @Test
    fun `sendCommand ChangePurpose updates purpose in uiState`() = runTest(UnconfinedTestDispatcher()) {
        val (uiModel, states) = createModel(backgroundScope)
        
        uiModel.sendCommand(AddTransactionCommand.ChangePurpose("Coffee"))
        
        val state = states.last()
        assertEquals("Coffee", state.purpose)
    }

    @Test
    fun `sendCommand ChangeValue updates value in uiState`() = runTest(UnconfinedTestDispatcher()) {
        val (uiModel, states) = createModel(backgroundScope)
        
        uiModel.sendCommand(AddTransactionCommand.ChangeValue("10.5"))
        
        val state = states.last()
        assertEquals("10.5", state.value)
    }

    @Test
    fun `sendCommand ChangeDate updates date in uiState`() = runTest {
        val (uiModel, states) = createModel(backgroundScope)
        
        uiModel.sendCommand(AddTransactionCommand.ChangeDate(123456789L))
        
        val state = states.last()
        assertEquals(listOf(), states)
        assertEquals(123456789L, state.date)
    }

    @Test
    fun `reset clears all fields in uiState`() = runTest(UnconfinedTestDispatcher()) {
        val (uiModel, states) = createModel(backgroundScope)
        
        uiModel.sendCommand(AddTransactionCommand.ChangePurpose("P"))
        uiModel.reset()
        val state = states.first()
        assertEquals("", state.purpose)
        assertEquals("", state.value)
        assertEquals(0L, state.date)
    }

    @Test
    fun `sendCommand Save triggers addRecordTransaction`() = runTest(UnconfinedTestDispatcher()) {
        val mockAdd = MockAddRecordTransaction()
        val (uiModel, states) = createModel(backgroundScope, addRecordTransaction = mockAdd)

        uiModel.sendCommand(AddTransactionCommand.Save("r1", "p1", "v1", 100L))
        val state = states.last()

        assertEquals("r1", mockAdd.lastRecordId)
        assertEquals("p1", mockAdd.lastPurpose)
        assertEquals("v1", mockAdd.lastValue)
        assertEquals(100L, mockAdd.lastDatetime)
        
        assertTrue(state.saveState is CommandState.Success)
    }

    @Test
    fun `successful save resets purpose and value in uiState`() = runTest(UnconfinedTestDispatcher()) {
        val mockAdd = MockAddRecordTransaction()
        val (uiModel, states) = createModel(backgroundScope, addRecordTransaction = mockAdd)

        uiModel.sendCommand(AddTransactionCommand.ChangePurpose("OldPurpose"))
        uiModel.sendCommand(AddTransactionCommand.ChangeValue("OldValue"))
        uiModel.sendCommand(AddTransactionCommand.Save("r1", "p1", "v1", 100L))
        
        val state = states.last()
        
        assertEquals("", state.purpose)
        assertEquals("", state.value)
    }

    @Test
    fun `record flow updates date if current date is out of period`() = runTest(UnconfinedTestDispatcher()) {
        val mockGetRecord = MockGetRecord()
        val (uiModel, states) = createModel(backgroundScope, getRecord = mockGetRecord)
        
        uiModel.sendCommand(AddTransactionCommand.ChangeDate(0L))

        val period = PeriodDate(10, 2023)
        mockGetRecord.stubbedRecord = TestRecordInfo(period = period)

        uiModel.sendCommand(AddTransactionCommand.ChangeRecord("r1"))
        val expectedStart = LocalDateTime(2023, 10, 1, 0, 0, 1)
            .toInstant(TimeZone.UTC).toEpochMilliseconds()
        
        val state = states.last()
        assertEquals(expectedStart, state.date)
    }

    @Test
    fun `purposeSuggestions updates when recordId or purpose changes`() = runTest {
        val mockSuggestions = MockGetTransactionSuggestions()
        val (uiModel, states) = createModel(backgroundScope, getTransactionSuggestions = mockSuggestions)
        
        mockSuggestions.suggestions = listOf("Coffee", "Cafe")
        
        uiModel.sendCommand(AddTransactionCommand.ChangeRecord("r1"))
        uiModel.sendCommand(AddTransactionCommand.ChangePurpose("C"))
        
        val state = states.last()
        assertEquals(listOf("Coffee", "Cafe"), state.purposeSuggestions)
    }

    private fun TestScope.createModel(
        scope: CoroutineScope,
        getTransactionSuggestions: GetTransactionSuggestions = MockGetTransactionSuggestions(),
        addRecordTransaction: AddRecordTransaction = MockAddRecordTransaction(),
        getRecord: GetRecord = MockGetRecord()
    ): Pair<AddTransactionUIModel, MutableList<AddTransactionUIState>> {
        val uiModel = AddTransactionUIModel(
            getTransactionSuggestions = getTransactionSuggestions,
            addRecordTransaction = addRecordTransaction,
            getRecord = getRecord,
            scope = scope
        )

        val states = mutableListOf<AddTransactionUIState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            uiModel.uiState.collect { states.add(it) }
        }
        return uiModel to states
    }
}
