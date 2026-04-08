package shiny.mc.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.InjectedParam
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.transaction.GetRecordTransactions
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RecordViewModel(
//    @InjectedParam val recordId: String,
    private val getRecord: GetRecord,
    private val getRecordTransactions: GetRecordTransactions,
) : ViewModel() {

    fun record(recordId: String) = getRecord.getRecord(recordId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5.seconds),
            null
        )

    fun transactions(recordId: String) = getRecordTransactions.transactions(recordId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5.seconds),
            emptyList()
        )
}