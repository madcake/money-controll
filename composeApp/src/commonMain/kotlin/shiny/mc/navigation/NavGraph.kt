package shiny.mc.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import shiny.mc.feature.categories.navigation.categories
import shiny.mc.feature.categories.navigation.openCategories
import shiny.mc.feature.period.navigation.PeriodNavKey
import shiny.mc.feature.period.navigation.period
import shiny.mc.feature.periods.navigation.openPeriods
import shiny.mc.feature.periods.navigation.periods
import shiny.mc.feature.record.navigation.openRecord
import shiny.mc.feature.record.navigation.record

@Composable
fun NavGraph() {
    val backStack = remember { mutableStateListOf<NavKey>(PeriodNavKey) }
    val onCancel = fun () { backStack.removeLastOrNull() }

    NavDisplay(
        modifier = Modifier.fillMaxSize().imePadding(),
        backStack = backStack,
        onBack = onCancel,
        entryProvider = entryProvider {
            period(
                openCategories = backStack::openCategories,
                openPeriods = backStack::openPeriods,
                openRecord = backStack::openRecord,
            )

            categories(
                onCancel = onCancel
            )

            periods(
                onCancel = onCancel,
            )

            record(
                onCancel = onCancel
            )
        }
    )
}