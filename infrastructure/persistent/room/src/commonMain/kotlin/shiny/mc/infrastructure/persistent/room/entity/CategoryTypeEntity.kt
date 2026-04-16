package shiny.mc.infrastructure.persistent.room.entity

import shiny.mc.core.dto.CategoryType

enum class CategoryTypeEntity {
    In,
    Out, ;
}

fun CategoryTypeEntity.toDto(): CategoryType = when (this) {
    CategoryTypeEntity.In -> CategoryType.In
    CategoryTypeEntity.Out -> CategoryType.Out
}

fun CategoryType.toEntity(): CategoryTypeEntity = when (this) {
    CategoryType.In -> CategoryTypeEntity.In
    CategoryType.Out -> CategoryTypeEntity.Out
}