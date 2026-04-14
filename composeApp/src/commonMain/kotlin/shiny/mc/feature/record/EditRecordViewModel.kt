package shiny.mc.feature.record

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.record.UpdateRecordValue
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class EditRecordViewModel(
    @InjectedParam private val recordId: String,
    private val getRecord: GetRecord,
    private val updateRecordValue: UpdateRecordValue,
) : ViewModel() {

    val estimateValueState = TextFieldState()
    private val estimateValue = snapshotFlow {
        estimateValueState.text.toString()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "0")

    val record = getRecord.getRecord(recordId)
        .onEach { record ->
            record?.let {
                if (record.estimateValue == 0.0) {
                    estimateValueState.clearText()
                } else {
                    estimateValueState.setTextAndPlaceCursorAtEnd(record.estimateValue.toString())
                }
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5.seconds), null)

    fun update(recordId: String) = viewModelScope.launch {
        val value = estimateValue.value
        updateRecordValue.update(recordId, value)
    }
}
