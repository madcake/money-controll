package shiny.mc.infrastructure.persistent.room.di

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import shiny.mc.infrastructure.persistent.room.RoomStore

actual val platformStoreModule = module {
    single<RoomDatabase.Builder<RoomStore>> {
        create(::getDatabaseBuilder)
    }
}

private fun getDatabaseBuilder(): RoomDatabase.Builder<RoomStore> {
    val dbFilePath = documentDirectory() + "/store.db"
    return Room.databaseBuilder<RoomStore>(
        name = dbFilePath,
    )
}
@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}