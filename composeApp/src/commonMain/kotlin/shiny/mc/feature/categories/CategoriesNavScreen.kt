package shiny.mc.feature.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.placeholders_string_filter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.feature.add_category.AddCategoryNavScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesNavScreen(
    month: Int,
    year: Int,
    onCancel: () -> Unit,
    viewModel: CategoriesViewModel = koinViewModel(),
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selected = remember { mutableStateListOf<Long>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Select categories") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "")
                    }
                },
                actions = {
                    if (selected.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.addToPeriod(categories, month, year)
                                onCancel()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = "")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            stickyHeader {
                AddCategoryNavScreen()
            }
            stickyHeader {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = viewModel.queryState,
                    placeholder = { Text(stringResource(Res.string.placeholders_string_filter)) },
                    trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") }
                )
            }
            items(categories) { item ->
                CategoryItem(
                    category = item,
                    isSelected = selected.contains(item.id),
                    onMenu = {
                        viewModel.remoteCategory(item.id)
                    }
                ) {
                    if (!selected.remove(item.id)) {
                        selected.add(item.id ?: return@CategoryItem)
                    }
                }
            }
        }
    }
}

