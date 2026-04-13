package shiny.mc.feature.record

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.navigation.OnCancel
import shiny.mc.core.domain.entity.Transaction
import shiny.mc.feature.add_transaction.AddTransactionNavScreen
import shiny.mc.platform.dateFormate
import shiny.mc.platform.format
import shiny.mc.theme.components.ColumnItem
import shiny.mc.theme.components.ColumnItemValue
import shiny.mc.theme.components.ItemPosition
import shiny.mc.theme.components.itemsPosition
import shiny.mc.theme.space

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RecordNavScreen(
    recordId: String,
    onCancel: OnCancel,
    viewModel: RecordViewModel = koinViewModel()//(key = recordId) { parametersOf(recordId) },
) {
    val record by viewModel.record(recordId).collectAsStateWithLifecycle()
    val transactions by viewModel.transactions(recordId).collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${record?.category?.title}",
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
        bottomBar = {
            Surface(
                shadowElevation = 1.dp,
            ) {
                AddTransactionNavScreen(
                    onCancel = {},
                    recordId = recordId,
                )
            }
        },
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
                TransactionItem(item, position)
            }
        }
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
private fun TransactionItem(
    tx: Transaction,
    position: ItemPosition,
) {
    ColumnItem(
        headline = tx.purpose,
        supporting = tx.datetime.dateFormate(),
        position = position,
        trailing = { ColumnItemValue(tx.value.format(), "") }
    )
}