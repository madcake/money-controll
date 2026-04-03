package shiny.mc.theme.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Modifier

actual fun Modifier.itemEventHandler(
    onMenu: (() -> Unit)?,
    onClick: () -> Unit
): Modifier = combinedClickable(
    onClick = onClick,
    onLongClick = onMenu,
)