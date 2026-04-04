package shiny.mc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.feature.categories.CategoriesNavScreen
import shiny.mc.feature.period.PeriodNavScreen
import shiny.mc.services.store.RoomStore
import shiny.mc.services.store.dao.ExpenseDao

@Serializable
object Root : NavKey

@Serializable
object Expenses : NavKey

class Categories(
    val month: Int,
    val year: Int,
) : NavKey

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
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<Expenses> {
                        PeriodNavScreen(
                            onCategories = { month, year ->
                                backStack.add(Categories(month, year))
                            }
                        )
                    }
                    entry<Root> {

                    }

                    entry<Categories> {
                        CategoriesNavScreen(it.month, it.year, onCancel = onCancel)
                    }
                }
            )
        }

    }
}