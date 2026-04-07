package shiny.mc.services.store.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.domain.entity.Category

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

data class RecordSummaryEntity(
    @PrimaryKey val id: String,
    val categoryId: Long,
    val month: Int,
    val year: Int,
    val scheduledValue: Double,
    val real: Double?,
    val title: String,
    val type: CategoryTypeEntity,
)

fun Record.toEntity(): RecordEntity = RecordEntity(
    id = id,
    categoryId = category.id!!,
    month = month,
    year = year,
    scheduledValue = scheduledValue,
)

fun List<Record>.toEntity() = map { it.toEntity() }

fun RecordSummaryEntity.toDto(): Record = Record(
    id = id,
    category = Category(
        id = categoryId,
        title = title,
        type = type.toDto(),
    ),
    month = month,
    year = year,
    scheduledValue = scheduledValue,
    realValue = real ?: 0.0,
)

fun List<RecordSummaryEntity>.toDto() = map { it.toDto() }

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<List<RecordSummaryEntity>>.toDto() = mapLatest { it.toDto() }