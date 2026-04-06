package shiny.mc.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.transaction.GetRecordTransactions

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class RecordViewModel(
    private val getRecord: GetRecord,
    private val getRecordTransactions: GetRecordTransactions,
    @InjectedParam private val recordId: String,
) : ViewModel() {

    val record = getRecord.getRecord(recordId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactions = record.filterNotNull()
        .flatMapLatest { getRecordTransactions.transactions(it.id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}