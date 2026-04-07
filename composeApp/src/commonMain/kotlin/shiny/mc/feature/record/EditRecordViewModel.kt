package shiny.mc.feature.record

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import shiny.mc.core.coordinators.record.GetRecord
import shiny.mc.core.coordinators.record.UpdateRecord

@KoinViewModel
class EditRecordViewModel(
    @InjectedParam private val recordId: String,
    private val getRecord: GetRecord,
    private val updateRecord: UpdateRecord,
) : ViewModel() {

    val scheduleValueState = TextFieldState()
    private val scheduleValue = snapshotFlow {
        scheduleValueState.text.toString()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "0")

    val record = getRecord.getRecord(recordId)
        .onEach { record ->
            record?.let {
                scheduleValueState.setTextAndPlaceCursorAtEnd(record.scheduledValue.toString())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), null)

    fun update() = viewModelScope.launch {
        record.value?.let {
            val value = scheduleValue.value
            updateRecord.update(it.copy(scheduledValue = value.toDouble()))
        }
    }
}
