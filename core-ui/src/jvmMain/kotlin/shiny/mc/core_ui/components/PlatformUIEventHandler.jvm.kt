package shiny.mc.core_ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.onClick
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton

@OptIn(ExperimentalFoundationApi::class)
actual fun Modifier.itemEventHandler(
    onMenu: (() -> Unit)?,
    onClick: (() -> Unit)?,
): Modifier {
    if (onMenu == null && onClick == null) {
        return Modifier
    }
    val modifier = onClick(matcher = PointerMatcher.Primary, onClick = onClick ?: {})
    return if (onMenu != null) {
        modifier.onClick(
            matcher = PointerMatcher.mouse(PointerButton.Secondary),
            onClick = onMenu
        )
    } else {
        modifier
    }
}