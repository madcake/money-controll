package shiny.mc.di

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import shiny.mc.services.store.RoomStore
import java.io.File

actual val platformModule = module {
    single<RoomDatabase.Builder<RoomStore>> {
        create(::getDatabaseBuilder)
    }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<RoomStore> {
    val dbFile = File(System.getProperty("user.home"), ".money_control")
    return Room.databaseBuilder<RoomStore>(
        name = dbFile.absolutePath,
    )
}
