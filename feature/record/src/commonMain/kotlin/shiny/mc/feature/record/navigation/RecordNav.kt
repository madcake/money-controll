package shiny.mc.feature.record.navigation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import shiny.mc.core_ui.model.OnCancel
import shiny.mc.feature.record.presentation.RecordNavScreen

@Serializable
class RecordNavKey(
    val recordId: String,
) : NavKey

fun MutableList<NavKey>.openRecord(recordId: String) {
    add(RecordNavKey(recordId))
}

fun EntryProviderScope<NavKey>.record(
    onTransaction: @Composable (String) -> Unit,
    onCancel: OnCancel,
) {
    entry<RecordNavKey> { entry ->
        RecordNavScreen(
            recordId = entry.recordId,
            onTransaction = onTransaction,
            onCancel = onCancel,
        )
    }
}