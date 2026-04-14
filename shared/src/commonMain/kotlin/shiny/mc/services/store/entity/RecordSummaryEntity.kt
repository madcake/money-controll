package shiny.mc.services.store.entity

import androidx.room.PrimaryKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.aggregate.Record
import shiny.mc.core.domain.entity.Category

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

fun RecordSummaryEntity.toDto(): Record = Record(
    id = id,
    category = Category(
        id = categoryId,
        title = title,
        type = type.toDto(),
    ),
    month = month,
    year = year,
    estimateValue = scheduledValue,
    realValue = real ?: 0.0,
)

fun List<RecordSummaryEntity>.toDto() = map { it.toDto() }

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<List<RecordSummaryEntity>>.toDto() = mapLatest { it.toDto() }