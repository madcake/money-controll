package shiny.mc.infrastructure.persistent.room.di

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import shiny.mc.infrastructure.persistent.room.RoomStore
import java.io.File

actual val platformStoreModule = module {
    single<RoomDatabase.Builder<RoomStore>> {
        create(::getDatabaseBuilder)
    }
}

private fun getDatabaseBuilder(): RoomDatabase.Builder<RoomStore> {
    val dbFile = File(System.getProperty("user.home") + File.separator + ".money_control", DATABASE_NAME)
    if (!dbFile.parentFile.exists()) {
        dbFile.parentFile.mkdirs()
    }
    return Room.databaseBuilder<RoomStore>(
        name = dbFile.absolutePath,
    )
}