package shiny.mc.feature.record.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.domain.entity.TransactionInfo
import shiny.mc.core_ui.components.ColumnItem
import shiny.mc.core_ui.components.ColumnItemValue
import shiny.mc.core_ui.components.ItemPosition
import shiny.mc.core_ui.components.SwipeableItem
import shiny.mc.core_ui.components.itemsPosition
import shiny.mc.core_ui.model.OnCancel
import shiny.mc.core_ui.theme.space

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RecordNavScreen(
    recordId: String,
    onCancel: OnCancel,
    onTransaction: @Composable (String) -> Unit,
    viewModel: RecordViewModel = koinViewModel()//(key = recordId) { parametersOf(recordId) },
) {
    val record by viewModel.record(recordId).collectAsStateWithLifecycle()
    val transactions by viewModel.transactions(recordId).collectAsStateWithLifecycle()

    var revealedItem by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${record?.title}",
                        maxLines = 2,
                        overflow = TextOverflow.MiddleEllipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onCancel) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "")
                    }
                },
            )
        },
        bottomBar = { onTransaction(recordId) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(MaterialTheme.space.paddingDefault),
            verticalArrangement = MaterialTheme.space.dividerArrangement,
        ) {
            record?.let {
                item {
                    RecordHeader(recordId = it.id)
                }
            }
            itemsPosition(transactions) { position, item ->
                TransactionItem(
                    tx = item,
                    position = position,
                    isRevealed = item.id == revealedItem,
                    onReveal = { state -> revealedItem = item.id.takeIf { !state } },
                    onDelete = { viewModel.delete(item.id) }
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(
    tx: TransactionInfo,
    position: ItemPosition,
    isRevealed: Boolean,
    onReveal: (Boolean) -> Unit,
    onDelete: () -> Unit,
) {
    SwipeableItem(
        isRevealed,
        itemPosition = position,
        onReveal = onReveal,
        backgroundContent = {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Delete transaction",
                )
            }
        }
    ) { position ->
        ColumnItem(
            headline = tx.title,
            supporting = tx.date,
            position = position,
            trailing = { ColumnItemValue(tx.value, "") }
        )
    }
}