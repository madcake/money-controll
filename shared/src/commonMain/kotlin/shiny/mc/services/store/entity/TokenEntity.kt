package shiny.mc.services.store.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
        )
    ]
)
class TokenEntity(
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val title: String,
    val count: Float,
    val pricePerItem: Float,
    val itemType: ItemTypeEntity,
)

enum class ItemTypeEntity {
    Gram,
    Kg,
    Liter,
    Pkg,
    Min,
    Hour,
    Month,
}