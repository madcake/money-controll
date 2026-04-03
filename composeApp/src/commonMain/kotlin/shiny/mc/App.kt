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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource

import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.feature.categories.CategoriesNavScreen
import shiny.mc.feature.records.ExpensesListNavScreen
import shiny.mc.services.store.RoomStore
import shiny.mc.services.store.dao.ExpenseDao

@Serializable
object Root : NavKey

@Serializable
object Expenses : NavKey

object Categories : NavKey

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
                        ExpensesListNavScreen(
                            onCategories = { backStack.add(Categories) }
                        )
                    }
                    entry<Root> {
                        Root({ backStack.add(Expenses) })
                    }

                    entry<Categories> {
                        CategoriesNavScreen(onCancel = onCancel)
                    }
                }
            )
        }

    }
}

@Composable
fun Expenses(obj: Expenses) {
    val viewModel = koinViewModel<SimpleViewModel>()
    val items by viewModel.items.collectAsStateWithLifecycle();
    Column {
        items.forEach {
            Text(it.title)
        }
    }
}

@Composable
fun Root(
    onExpenses: () -> Unit,
) {
    val store = koinInject<RoomStore>()
    val dao = koinInject<ExpenseDao>()
    val viewModel = koinViewModel<SimpleViewModel>()
    dao.getExpanses()
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        Button(onExpenses) {
            Text("Expenses")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}