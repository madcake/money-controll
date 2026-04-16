package shiny.mc.infrastructure.persistent.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_config")
data class AppConfigEntity(
    @PrimaryKey val key: AppConfigKeyEntity,
    val value: String?,
)