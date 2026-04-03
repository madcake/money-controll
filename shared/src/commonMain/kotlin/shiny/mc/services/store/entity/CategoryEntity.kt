@file:OptIn(ExperimentalCoroutinesApi::class)

package shiny.mc.services.store.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import shiny.mc.core.domain.entity.Category

@Entity(
    tableName = "category",
    indices = [
        Index(value = ["title"], unique = true),
    ])
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: CategoryTypeEntity,
)

fun CategoryEntity.toDto(): Category = Category(
    id = id,
    title = title,
    type = type.toDto()
)

fun List<CategoryEntity>.toDto() = map { it.toDto() }

fun Flow<List<CategoryEntity>>.toDto() = mapLatest { items -> items.toDto() }

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id ?: 0,
    title = title,
    type = type.toEntity()
)

fun List<Category>.toEntity() = map { it.toEntity() }