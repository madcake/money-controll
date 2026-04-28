package shiny.mc.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import shiny.mc.feature.category.navigation.categories
import shiny.mc.feature.category.navigation.openCategories
import shiny.mc.feature.period.navigation.PeriodNavKey
import shiny.mc.feature.period.navigation.openPeriods
import shiny.mc.feature.period.navigation.period
import shiny.mc.feature.period.navigation.periods
import shiny.mc.feature.record.navigation.openRecord
import shiny.mc.feature.record.navigation.record
import shiny.mc.feature.transaction.add_transaction.AddTransactionNavScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NavGraph() {
    val backStack = remember { mutableStateListOf<NavKey>(PeriodNavKey) }
    val onCancel = fun () { backStack.removeLastOrNull() }
    val sceneStrategy = rememberListDetailSceneStrategy<NavKey>()

    NavDisplay(
        modifier = Modifier.fillMaxSize().imePadding(),
        backStack = backStack,
        onBack = onCancel,
        sceneStrategies = listOf(sceneStrategy),
        entryProvider = entryProvider {
            period(
                openCategories = backStack::openCategories,
                openPeriods = backStack::openPeriods,
                openRecord = backStack::openRecord,
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "Select record",
                            )
                        }
//                            DetailPaneContent(selectedItem = null, onShowExtra = {})
                    }
                )
            )

            categories(
                onCancel = onCancel
            )

            periods(
                onCancel = onCancel,
            )

            record(
                onCancel = onCancel,
                onTransaction = { recordId ->
                    Surface(
                        shadowElevation = 1.dp,
                    ) {
                        AddTransactionNavScreen(
                            onCancel = {},
                            recordId = recordId,
                        )
                    }
                },
                metadata = ListDetailSceneStrategy.detailPane(),
            )
        }
    )
}