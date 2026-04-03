package shiny.mc

import android.app.Application
import org.koin.android.ext.koin.androidContext
import shiny.mc.di.initKoin

class McApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@McApp)
        }
    }
}