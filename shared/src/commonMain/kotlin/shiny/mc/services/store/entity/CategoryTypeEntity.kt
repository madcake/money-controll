package shiny.mc.services.store.entity

import shiny.mc.core.domain.value.CategoryType
import shiny.mc.services.store.entity.CategoryTypeEntity.Asset
import shiny.mc.services.store.entity.CategoryTypeEntity.Liability

enum class CategoryTypeEntity {
    Asset,
    Liability, ;
}

fun CategoryTypeEntity.toDto(): CategoryType = when (this) {
    Asset -> CategoryType.Asset
    Liability -> CategoryType.Liability
}

fun CategoryType.toEntity(): CategoryTypeEntity = when (this) {
    CategoryType.Asset -> Asset
    CategoryType.Liability -> Liability
}