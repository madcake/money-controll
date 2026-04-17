package shiny.mc.core_ui.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Modifier

actual fun Modifier.itemEventHandler(
    onMenu: (() -> Unit)?,
    onClick: (() -> Unit)?,
): Modifier = if (onMenu == null && onClick == null) {
    return Modifier
} else {
    combinedClickable(
        onClick = onClick ?: {},
        onLongClick = onMenu,
    )
}