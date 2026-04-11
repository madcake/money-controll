package shiny.mc.theme.components

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable

open class ItemPosition(val index: Int, val count: Int) {

    object Single : ItemPosition(0, 1)
}

fun <T> LazyListScope.itemsPosition(
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    itemContent: @Composable LazyItemScope.(position: ItemPosition, item: T) -> Unit,
) {
    val count = items.size
    itemsIndexed(items, key, contentType) { index, item ->
        itemContent(ItemPosition(index, count), item)
    }
}