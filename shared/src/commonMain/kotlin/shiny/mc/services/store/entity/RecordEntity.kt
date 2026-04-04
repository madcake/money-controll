package shiny.mc.services.store.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.aggregate.CategoryRecord
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

data class RecordCategoryEntity(
    @PrimaryKey val id: String,
    val categoryId: Long,
    val month: Int,
    val year: Int,
    val scheduledValue: Double,
    val title: String,
    val type: CategoryTypeEntity,
)

fun CategoryRecord.toEntity(): RecordEntity = RecordEntity(
    id = id,
    categoryId = category.id!!,
    month = month,
    year = year,
    scheduledValue = scheduledValue,
)

fun List<CategoryRecord>.toEntity() = map { it.toEntity() }

fun RecordCategoryEntity.toDto(): CategoryRecord = CategoryRecord(
    id = id,
    category = Category(
        id = categoryId,
        title = title,
        type = type.toDto(),
    ),
    month = month,
    year = year,
    scheduledValue = scheduledValue
)

fun List<RecordCategoryEntity>.toDto() = map { it.toDto() }

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<List<RecordCategoryEntity>>.toDto() = mapLatest { it.toDto() }