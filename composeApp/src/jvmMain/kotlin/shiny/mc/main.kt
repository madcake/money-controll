package shiny.mc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.plugin.module.dsl.startKoin
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