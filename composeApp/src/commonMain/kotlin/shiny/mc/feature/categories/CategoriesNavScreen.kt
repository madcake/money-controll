package shiny.mc.feature.categories

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import moneycontroll.composeapp.generated.resources.Res
import moneycontroll.composeapp.generated.resources.placeholders_category_title
import moneycontroll.composeapp.generated.resources.placeholders_string_filter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.feature.add_category.AddCategoryNavScreen

@Composable
fun CategoriesNavScreen(
    onCancel: () -> Unit,
    viewModel: CategoriesViewModel = koinViewModel(),
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.padding(16.dp),
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
                onMenu = {
                    viewModel.remoteCategory(item.id)
                }
            )
        }
    }
}

