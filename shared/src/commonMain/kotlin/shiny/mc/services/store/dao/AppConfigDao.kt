package shiny.mc.services.store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shiny.mc.services.store.entity.AppConfigEntity
import shiny.mc.services.store.entity.AppConfigKeyEntity

@Dao
interface AppConfigDao {

    @Query("""
        SELECT value FROM app_config WHERE `key` = :name
    """)
    fun get(name: AppConfigKeyEntity): Flow<String?>

    @Insert(onConflict = REPLACE)
    suspend fun insert(entity: AppConfigEntity)


}