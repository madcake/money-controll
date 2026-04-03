package shiny.mc.services.store.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = CASCADE,
        )
    ]
)
class CategoryRecordEntity(
    @PrimaryKey val id: String,
    val categoryId: Int,
    val startDatetime: Long,
    val endDatetime: Long,
    val scheduledValue: Double,
)