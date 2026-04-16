package shiny.mc.infrastructure.persistent.room.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import shiny.mc.infrastructure.persistent.room.RoomStore

actual val platformStoreModule = module {
    single<RoomDatabase.Builder<RoomStore>> {
        create(::getDatabaseBuilder)
    }
}

private fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<RoomStore> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("store.db")
    return Room.databaseBuilder<RoomStore>(
        context = appContext,
        name = dbFile.absolutePath
    )
}