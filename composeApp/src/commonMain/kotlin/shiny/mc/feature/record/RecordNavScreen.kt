package shiny.mc.feature.record

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import shiny.mc.core.domain.entity.Transaction
import shiny.mc.feature.add_transaction.AddTransactionNavScreen
import shiny.mc.navigation.OnCancel
import shiny.mc.platform.dateFormate
import shiny.mc.platform.format
import shiny.mc.theme.components.ColumnItem
import shiny.mc.theme.components.ColumnItemValue
import shiny.mc.theme.components.ItemPosition
import shiny.mc.theme.components.itemsPosition
import shiny.mc.theme.space
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RecordNavScreen(
    recordId: String,
    onCancel: OnCancel,
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
    tx: Transaction,
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
            headline = tx.purpose,
            supporting = tx.datetime.dateFormate(),
            position = position,
            trailing = { ColumnItemValue(tx.value.format(), "") }
        )
    }
}

@Composable
fun SwipeableItem(
    isRevealed: Boolean,
    backgroundContent: @Composable RowScope.() -> Unit,
    itemPosition: ItemPosition,
    onReveal: (Boolean) -> Unit,
    content: @Composable (ItemPosition) -> Unit
) {

    var contextMenuWidth by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(initialValue = 0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isRevealed, contextMenuWidth) {
        if (isRevealed) {
            offset.animateTo(contextMenuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .onSizeChanged { contextMenuWidth = -it.width.toFloat() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                backgroundContent()
            }
        }
        Surface(
            modifier = Modifier
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(contextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(contextMenuWidth, 0f)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value <= contextMenuWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(contextMenuWidth)
                                        onReveal(true)
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onReveal(false)
                                    }
                                }
                            }
                        }
                    )
                },
        ) {
            content(itemPosition)
        }
    }
}