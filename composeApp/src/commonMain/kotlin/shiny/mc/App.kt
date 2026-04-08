package shiny.mc

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import shiny.mc.feature.categories.CategoriesNavScreen
import shiny.mc.feature.period.PeriodNavScreen
import shiny.mc.feature.record.RecordNavScreen

@Serializable
object Expenses : NavKey

class Categories(
    val month: Int,
    val year: Int,
) : NavKey

class Record(
    val recordId: String,
) : NavKey

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold { innerPadding ->
            val backStack = remember { mutableStateListOf<NavKey>(Expenses) }
            val onCancel = fun () { backStack.removeLastOrNull() }
            NavDisplay(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
                backStack = backStack,
                onBack = onCancel,
                entryProvider = entryProvider {
                    entry<Expenses> {
                        PeriodNavScreen(
                            onCategories = { month, year ->
                                backStack.add(Categories(month, year))
                            },
                            onRecord = { recordId ->
                                backStack.add(Record(recordId))
                            }
                        )
                    }

                    entry<Categories> {
                        CategoriesNavScreen(it.month, it.year, onCancel = onCancel)
                    }

                    entry<Record> { entry ->
                        RecordNavScreen(entry.recordId, onCancel)
                    }
                }
            )
        }

    }
}