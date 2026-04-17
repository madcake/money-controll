package shiny.mc.core_ui.components

import androidx.compose.ui.Modifier

expect fun Modifier.itemEventHandler(onMenu: (() -> Unit)? = null, onClick: (() -> Unit)?): Modifier