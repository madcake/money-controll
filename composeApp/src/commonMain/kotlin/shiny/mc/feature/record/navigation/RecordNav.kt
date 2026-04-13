package shiny.mc.feature.record.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import shiny.mc.navigation.OnCancel
import shiny.mc.feature.record.RecordNavScreen

@Serializable
class RecordNavKey(
    val recordId: String,
) : NavKey

fun MutableList<NavKey>.openRecord(recordId: String) {
    add(RecordNavKey(recordId))
}

fun EntryProviderScope<NavKey>.record(
    onCancel: OnCancel
) {
    entry<RecordNavKey> { entry ->
        RecordNavScreen(entry.recordId, onCancel)
    }
}