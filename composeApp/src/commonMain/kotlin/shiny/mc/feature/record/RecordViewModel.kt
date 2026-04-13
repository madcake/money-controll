package shiny.mc.feature.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.transaction.DeleteTransaction
import shiny.mc.core.coordinators.transaction.GetRecordTransactions
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class RecordViewModel(
// TODO: wait koin fix    @InjectedParam val recordId: String,
    private val getRecord: GetRecord,
    private val getRecordTransactions: GetRecordTransactions,
    private val deleteTransaction: DeleteTransaction,
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

    fun delete(transactionId: Long?) = viewModelScope.launch {
        transactionId ?: return@launch
        deleteTransaction.delete(transactionId)
    }
}