package shiny.mc.theme.components

import androidx.compose.ui.Modifier

expect fun Modifier.itemEventHandler(onMenu: (() -> Unit)? = null, onClick: (() -> Unit)?): Modifier