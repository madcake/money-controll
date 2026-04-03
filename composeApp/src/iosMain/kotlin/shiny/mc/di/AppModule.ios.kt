package shiny.mc.di

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import shiny.mc.services.store.RoomStore

actual val platformModule = module {
    single<RoomDatabase.Builder<RoomStore>> {
        getDatabaseBuilder()
    }
}

fun getDatabaseBuilder(): RoomDatabase.Builder<RoomStore> {
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