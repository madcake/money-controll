package shiny.mc.feature.period.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import shiny.mc.core.domain.value.PeriodDate
import shiny.mc.feature.period.PeriodNavScreen

@Serializable
object PeriodNavKey : NavKey

//fun MutableList<NavKey>.openPeriod() {
//    add(PeriodNavKey)
//}

fun EntryProviderScope<NavKey>.period(
    openCategories: (PeriodDate) -> Unit,
    openPeriods: () -> Unit,
    openRecord: (String) -> Unit,
) {
    entry<PeriodNavKey> {
        PeriodNavScreen(
            onCategories = openCategories,
            onRecord = openRecord,
            onPeriods = openPeriods,
        )
    }
}