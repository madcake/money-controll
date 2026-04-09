package shiny.mc.services.store.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import shiny.mc.core.domain.aggregate.Record

@Entity(
    tableName = "record",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = CASCADE,
        )
    ]
)
class RecordEntity(
    @PrimaryKey val id: String,
    val categoryId: Long,
    val month: Int,
    val year: Int,
    val scheduledValue: Double,
)

fun Record.toEntity(): RecordEntity = RecordEntity(
    id = id,
    categoryId = category.id!!,
    month = month,
    year = year,
    scheduledValue = scheduledValue,
)

fun List<Record>.toEntity() = map { it.toEntity() }