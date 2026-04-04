package shiny.mc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import shiny.mc.di.initKoin

fun main() = application {
    initKoin {  }

    Window(
        onCloseRequest = ::exitApplication,
        title = "MoneyControll",
    ) {
        App()
    }
}