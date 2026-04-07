package shiny.mc.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.transaction.GetRecordTransactions

@OptIn(ExperimentalCoroutinesApi::class)
//@KoinViewModel
class RecordViewModel(
    private val getRecord: GetRecord,
    private val getRecordTransactions: GetRecordTransactions,
//    @InjectedParam private val recordId: String,
) : ViewModel() {

    fun record(recordId: String) = getRecord.getRecord(recordId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(300), null)

    fun transactions(recordId: String)  = getRecordTransactions.transactions(recordId)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
}