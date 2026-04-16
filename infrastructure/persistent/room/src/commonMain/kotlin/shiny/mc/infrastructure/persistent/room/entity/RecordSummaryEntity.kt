package shiny.mc.infrastructure.persistent.room.entity

import androidx.room.PrimaryKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.dto.Category
import shiny.mc.core.dto.Record

data class RecordSummaryEntity(
    @PrimaryKey val id: String,
    val categoryId: Long,
    val month: Int,
    val year: Int,
    val estimateValue: Double,
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
    estimateValue = estimateValue,
    realValue = real ?: 0.0,
)

fun List<RecordSummaryEntity>.toDto() = map { it.toDto() }

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<List<RecordSummaryEntity>>.toDto() = mapLatest { it.toDto() }