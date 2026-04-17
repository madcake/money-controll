package shiny.mc.feature.periods.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import shiny.mc.core_ui.model.OnCancel
import shiny.mc.feature.periods.PeriodsNavScreen

@Serializable
object PeriodsNavKey : NavKey

fun MutableList<NavKey>.openPeriods() {
    add(PeriodsNavKey)
}

fun EntryProviderScope<NavKey>.periods(
    onCancel: OnCancel,
) {
    entry<PeriodsNavKey> {
        PeriodsNavScreen(onCancel = onCancel)
    }
}