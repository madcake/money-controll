package shiny.mc.feature.record

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import shiny.mc.core.domain.aggregate.CategoryRecord
import shiny.mc.core.domain.entity.Transaction
import shiny.mc.feature.add_transaction.AddTransactionNavScreen
import shiny.mc.theme.components.ColumnItem
import shiny.mc.theme.components.ColumnItemValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RecordNavScreen(
    recordId: String,
    onCancel: () -> Unit,
    viewModel: RecordViewModel = koinViewModel(key = recordId, parameters = { parametersOf(recordId) })
) {
    val record by viewModel.record.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TwoRowsTopAppBar(
                title = {
                    Text(
                        text = "${record?.category?.title}",
                        overflow = TextOverflow.MiddleEllipsis,
                    )
                },
                subtitle = { expanded ->
                    if (expanded) {
                        Text(
                            text = "${record?.month}-${record?.year}",
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onCancel) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "")
                    }
                },
                actions = {
                    IconButton({}) {
                        Icon(imageVector = Icons.AutoMirrored.Default.PlaylistAdd, contentDescription = "")
                    }
                }
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
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                RecordHeader(record)
            }
            items(transactions) { tx ->
                TransactionItem(tx)
            }
        }
    }
}

@Composable
private fun RecordHeader(record: CategoryRecord?) {
    record ?: return

}

@Composable
private fun TransactionItem(tx: Transaction) {
    ColumnItem(
        headline = tx.purpose,
        supporting = tx.datetime.toString(),
        trailing = { ColumnItemValue(tx.value.toString(), "") }
    )
}