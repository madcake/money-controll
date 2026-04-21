package shiny.mc

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import shiny.mc.di.initKoin

fun main() = application {
    initKoin {  }

    Window(
        onCloseRequest = ::exitApplication,
        title = "MoneyControll",
        state = rememberWindowState(width = 1024.dp, height = 768.dp)
    ) {
        App()
    }
}
