package shiny.mc.feature.category.presentation.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberContainedSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.dto.PeriodDate
import shiny.mc.core_ui.components.itemsPosition
import shiny.mc.core_ui.resources.Res
import shiny.mc.core_ui.resources.placeholders_string_filter
import shiny.mc.core_ui.theme.space
import shiny.mc.feature.category.presentation.add_category.AddCategoryNavScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesNavScreen(
    date: PeriodDate,
    onCancel: () -> Unit,
    viewModel: CategoriesViewModel = koinViewModel(),
) {
    val inCategories by viewModel.inCategories.collectAsStateWithLifecycle()
    val outCategories by viewModel.outCategories.collectAsStateWithLifecycle()
    val selected by viewModel.selected(date.month, date.year).collectAsStateWithLifecycle()

    val searchBarState = rememberContainedSearchBarState()
    val appBarWithSearchColors = SearchBarDefaults.appBarWithSearchColors(
        searchBarColors = SearchBarDefaults.containedColors(state = searchBarState)
    )
    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = viewModel.queryState,
            searchBarState = searchBarState,
            colors = appBarWithSearchColors.searchBarColors.inputFieldColors,
            onSearch = { },
            placeholder = {
                Text(
                    modifier = Modifier.clearAndSetSemantics {},
                    text = stringResource(Res.string.placeholders_string_filter)
                )
            },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            },
        )
    }

    Scaffold(
        topBar = {
            AppBarWithSearch(
                state = searchBarState,
                colors = appBarWithSearchColors,
                inputField = inputField,
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "")
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(MaterialTheme.space.paddingDefault)
            ) {
                AddCategoryNavScreen()
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(MaterialTheme.space.paddingDefault),
            verticalArrangement = MaterialTheme.space.dividerArrangement
        ) {
            itemsPosition(inCategories) { position, item ->
                CategoryItem(
                    category = item,
                    isSelected = selected.contains(item.id),
                    position = position,
                ) {
                    viewModel.addToPeriod(item.id, date.month, date.year)
                }
            }

            item { if (inCategories.isNotEmpty()) MaterialTheme.space.groupSpace() }

            itemsPosition(outCategories) { position, item ->
                CategoryItem(
                    category = item,
                    isSelected = selected.contains(item.id),
                    position = position,
                ) {
                    viewModel.addToPeriod(item.id, date.month, date.year)
                }
            }
        }
    }
}

